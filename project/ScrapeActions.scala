import org.jsoup.helper.HttpConnection
import org.jsoup.nodes.Document
import org.jsoup.{Connection, HttpStatusException}

import java.net.URL
import java.util.concurrent.Executors
import scala.annotation.tailrec
import scala.collection.JavaConverters.*
import scala.concurrent.duration.*
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

/** Scrape the available set of actions from the Mergify website
  */
object ScrapeActions {
  def getJsoupDocument(url: URL): Document = {
    val originalUrlString = url.toString

    @tailrec
    def get(urlString: String): Document = {
      println(s"[$originalUrlString] Getting " + urlString)
      val connection = new HttpConnection()
      connection.url(urlString).followRedirects(true).method(Connection.Method.GET).ignoreHttpErrors(true)
      connection.execute()
      connection.response().statusCode() match {
        case s if s >= 200 && s < 300 => connection.response().parse()
        case 429                      =>
          val retryAfter = connection.response().header("Retry-After").toInt
          Console.err.println(s"[$originalUrlString] Too many requests, waiting $retryAfter seconds...")
          Thread.sleep(retryAfter * 1000)
          get(urlString)
        case s =>
          val message = s"HTTP error fetching URL (${connection.response().statusMessage()})"
          throw new HttpStatusException(message, s, urlString)
      }
    }

    @tailrec
    def followMetaRefresh(urlString: String): Document = {
      val doc       = get(urlString)
      val canonical = doc.select("html > head > meta[http-equiv=refresh]").first()
      if (canonical == null) doc
      else {
        val href = canonical.attr("content").split("=").last
        if (href.equals(urlString)) doc
        else {
          println(s"[$originalUrlString] Following to $href")
          followMetaRefresh(href)
        }
      }
    }

    followMetaRefresh(originalUrlString)
  }

  def run(): List[String] = {
    val baseUrl = new URL("https://docs.mergify.com/")
    val index   = getJsoupDocument(baseUrl)

    val actionPages =
      index
        .select("a")
        .eachAttr("href")
        .asScala
        .toList
        .filter(_.startsWith("/workflow/actions/"))
        .map(baseUrl.toURI.resolve(_).toURL)

    val actionDocs = {
      implicit val executionContext: ExecutionContext =
        ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))
      Await.result(
        Future.traverse(actionPages) { url =>
          Future {
            println("Scraping " + url)
            getJsoupDocument(url)
          }.andThen {
            case Success(value)     => println("Finished " + url)
            case Failure(exception) =>
              Console.err.println("Failed for " + url + ":")
              exception.printStackTrace()
          }
        },
        20.seconds
      )
    }

    for (doc <- actionDocs) yield {
      val h1          = doc.select("h1").first()
      val name        = h1.ownText().split('_').map(_.capitalize).mkString
      val description = h1.nextElementSibling().text().trim

      val parametersHeader = doc.select("h2#parameters").first()
      val table            = parametersHeader.parent().nextElementSiblings().select("table").first()

      val rows =
        if (table == null)
          Nil
        else
          table
            .select("tbody > tr")
            .asScala
            .toList
            .map(_.select("td").asScala.toList.map(_.text()))

      def wrapText(initial: String, width: Int) = {
        @tailrec
        def loop(string: String, splitComments: Vector[String]): Vector[String] =
          if (string.length > width) {
            val i = string.lastIndexOf(" ", width)
            loop(string.substring(i).trim, splitComments :+ string.take(i))
          } else
            splitComments :+ string
        loop(initial, Vector.empty)
      }

      def mkComment(description: String, indent: String) =
        if (description.isEmpty) ""
        else
          wrapText(description, 114 - indent.length)
            .mkString(s"$indent/** ", s"\n$indent  * ", s"\n$indent  */\n")

      mkComment(description, "") +
        "case class " + name.replace(" ", "") +
        (for (case List(List(name, typ, default*), List(description)) <- rows.grouped(2)) yield {
          val camelCaseName = (name.split('_').toList match {
            case Nil          => ""
            case head :: tail => head + tail.map(_.capitalize).mkString
          }) match {
            case "type" => "`type`"
            case s      => s
          }

          val baseScalaType = typ match {
            case "list of string" | "list of template" | "list of branch names"                => "Seq[String]"
            case "boolean"                                                                     => "Boolean"
            case "string" | "template" | "merge method: merge, squash, rebase or fast-forward" => "String"
            case other if other.startsWith("list of ") => "Seq[ToJson /*" + other.stripPrefix("list of ") + "*/]"
            case other                                 => "ToJson /*" + other + "*/"
          }

          val (scalaTypeAndDefault, commentDefault) =
            (baseScalaType, default.headOption.getOrElse("").trim) match {
              case ("String", "") => (s"""$baseScalaType = """"", None)
              case (_, "")        => (baseScalaType, None)
              case (_, d)         => (s"Option[$baseScalaType] = None", Some(d))
            }

          val defaultMsg = commentDefault.fold("")("\nDefault: " + _)

          "\n" + mkComment(description + defaultMsg, "  ") + "  " +
            camelCaseName + ": " + scalaTypeAndDefault
        })
          .mkString("(", ",", "\n)")
    }
  }
}

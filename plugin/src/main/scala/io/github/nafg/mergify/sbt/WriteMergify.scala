package io.github.nafg.mergify.sbt

import io.github.nafg.mergify.dsl._
import sbt._
import sbtghactions.GenerativeKeys.{githubWorkflowGenerate, githubWorkflowGeneratedCI}
import sbtghactions.{GenerativePlugin, WorkflowJob}


object WriteMergify extends AutoPlugin {
  override def requires = GenerativePlugin

  override def trigger = allRequirements

  def createMergify(job: WorkflowJob) =
    defaultMergify
      .addPullRequestRule("Automatically merge successful scala-steward PRs")(defaultQueueAction)(
        (Attr.Author :== "scala-steward") +:
          (for (o <- job.oses; s <- job.scalas; v <- job.javas) yield
            Attr.CheckSuccess :== s"${job.name} ($o, $s, ${v.render})"): _*
      )

  override def projectSettings = Seq(
    githubWorkflowGenerate := {
      githubWorkflowGenerate.value
      for (job <- githubWorkflowGeneratedCI.value if job.id == "build")
        IO.write(file(".mergify.yml"), createMergify(job).toYaml)
    }
  )
}

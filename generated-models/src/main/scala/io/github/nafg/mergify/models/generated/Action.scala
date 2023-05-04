// GENERATED CODE: DO NOT EDIT THIS FILE MANUALLY.
// TO UPDATE, RUN `sbt generateModels`
// SEE `project/ScrapeActions.scala`

package io.github.nafg.mergify.models.generated

import io.github.nafg.mergify.ToJson


sealed trait Action
object Action {
  /** The assign action assigns users to the pull request.
    */
  case class Assign(
    /** The users to assign to the pull request.
      */
    addUsers: Seq[String],
    /** The users to remove from assignees.
      */
    removeUsers: Seq[String]
  ) extends Action


  /** It is common for software to have (some of) their major versions maintained over an extended period. Developers
    * usually create stable branches that are maintained for a while by cherry-picking patches from the development
    * branch.
    */
  case class Backport(
    /** Users to assign the newly created pull request. As the type is Template, you could use, e.g., {{author}} to
      * assign the pull request to its original author.
      */
    assignees: Seq[String],
    /** The pull request body.
      */
    body: String = "This is an automatic backport of pull request #{{number}} done by [Mergify](https://mergify.com).\n{{cherry_pick_error}}",
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to backport a pull request. If no bot_account is
      * set, Mergify backports the pull request itself.
      */
    botAccount: String = "",
    /** The list of branches the pull request should be copied to.
      */
    branches: Seq[String] = Nil,
    /** Whether to create the pull requests even if they are conflicts when cherry-picking the commits.
      */
    ignoreConflicts: Boolean = true,
    /** The list of labels to add to the created pull requests.
      */
    labels: Seq[String] = Nil,
    /** The label to add to the created pull request if it has conflicts and ignore_conflicts is set to true.
      */
    labelConflicts: String = "conflicts",
    /** The list of regexes to find branches the pull request should be copied to.
      */
    regexes: Seq[String] = Nil,
    /** The pull request title.
      */
    title: String = "{{ title }} (backport #{{ number }})"
  ) extends Action


  /** The close action closes the pull request — without merging it.
    */
  case class Close(
    /** The message to write as a comment after closing the pull request.
      */
    message: String = "This pull request has been automatically closed by Mergify."
  ) extends Action


  /** The comment action posts a comment to the pull request.
    */
  case class Comment(
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to comment a pull request. If no bot_account is
      * set, Mergify will comment the pull request itself.
      */
    botAccount: String = "",
    /** The message to write as a comment.
      */
    message: String = ""
  ) extends Action


  /** The copy action creates a copy of the pull request targeting other branches.
    */
  case class Copy(
    /** Users to assign the newly created pull request. As the type is Template, you could use, e.g., {{author}} to
      * assign the pull request to its original author.
      */
    assignees: Seq[String],
    /** The pull request body.
      */
    body: String = "This is an automatic copy of pull request #{{number}} done by [Mergify](https://mergify.com).\n{{cherry_pick_error}}",
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to copy a pull request. If no bot_account is
      * set, Mergify copies the pull request itself.
      */
    botAccount: String = "",
    /** The list of branches the pull request should be copied to.
      */
    branches: Seq[String] = Nil,
    /** Whether to create the pull requests even if they are conflicts when cherry-picking the commits.
      */
    ignoreConflicts: Boolean = true,
    /** The list of labels to add to the created pull requests.
      */
    labels: Seq[String] = Nil,
    /** The label to add to the created pull request if it has conflicts and ignore_conflicts is set to true.
      */
    labelConflicts: String = "conflicts",
    /** The list of regexes to find branches the pull request should be copied to.
      */
    regexes: Seq[String] = Nil,
    /** The pull request title.
      */
    title: String = "{{ title }} (copy #{{ number }})"
  ) extends Action


  /** The delete_head_branch action deletes the head branch of the pull request, that is the branch which hosts the
    * commits. This only works if the branch is stored in the same repository that the pull request target, i.e., if the
    * pull request comes from the same repository and not from a fork.
    */
  case class DeleteHeadBranch(
    /** If set to true, the branch will be deleted even if another pull request depends on the head branch. GitHub will
      * therefore close the dependent pull requests.
      */
    force: Boolean = false
  ) extends Action


  /** The dismiss_reviews action removes reviews done by collaborators when the pull request is updated. This is
    * especially useful to make sure that a review does not stay when the branch is updated (e.g., new commits are added
    * or the branch is rebased).
    */
  case class DismissReviews(
    /** If set to true, all the approving reviews will be removed when the pull request is updated. If set to false,
      * nothing will be done. If set to a list, each item should be the GitHub login of a user whose review will be
      * removed. If set to from_requested_reviewers, the list of requested reviewers will be used to get whose review
      * will be removed.
  Default: true
      */
    approved: Option[ToJson /*Boolean or list of string*/] = None,
    /** If set to true, all the reviews requesting changes will be removed when the pull request is updated. If set to
      * false, nothing will be done. If set to a list, each item should be the GitHub login of a user whose review will
      * be removed. If set to from_requested_reviewers, the list of requested reviewers will be used to get whose review
      * will be removed.
  Default: true
      */
    changesRequested: Option[ToJson /*Boolean or list of string*/] = None,
    /** The message to post when dismissing the review.
      */
    message: String = "Pull request has been modified.",
    /** If set to synchronize, the action will run only if the pull request commits changed. Otherwise, it will run each
      * time the rule matches.
  Default: synchronize
      */
    when: Option[ToJson /*synchronize or always*/] = None
  ) extends Action


  /** The edit action changes some specific attributes on the pull request.
    */
  case class Edit(
    /** If the pull request should be a draft (true) or the other way around (false).
  Default: None
      */
    draft: Option[Boolean] = None
  ) extends Action


  /** The label action can add or remove labels from a pull request.
    */
  case class Label(
    /** The list of labels to add.
      */
    add: Seq[String] = Nil,
    /** The list of labels to remove.
      */
    remove: Seq[String] = Nil,
    /** Remove all labels from the pull request.
      */
    removeAll: Boolean = false,
    /** Toggle labels in the list based on the conditions. If all the conditions are a success, all the labels in the
      * list will be added, otherwise, they will all be removed.
      */
    toggle: Seq[String] = Nil
  ) extends Action


  /** The merge action merges the pull request into its base branch.
    */
  case class Merge(
    /** Template to use as the commit message when using the merge or squash merge method. Template can also be defined
      * in the pull request body (see Defining the Commit Message).
      */
    commitMessageTemplate: String = "",
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to merge pull request. If no merge_bot_account
      * is set, Mergify will merge the pull request itself. The user account must have already been logged in Mergify
      * dashboard once and have write or maintain permission.
      */
    mergeBotAccount: String = "",
    /** Merge method to use. Possible values are merge, squash, rebase or fast-forward.
      */
    method: String = "merge",
    /** Allow merging Mergify configuration change.
  Default: false
      */
    allowMergingConfigurationChange: Option[ToJson /*bool*/] = None
  ) extends Action


  case class PostCheck(
    /** List of conditions to match to mark the pull request check as succeeded, otherwise, it will be marked as
      * failing. If unset, the conditions from the rule that triggers this action are used.
      */
    successConditions: ToJson /*list of conditions*/,
    /** The summary of the check.
      */
    summary: String = "",
    /** The title of the check.
      */
    title: String = ""
  ) extends Action


  /** The queue action moves the pull request into one of the merge queue defined in Queue Rules.
    */
  case class Queue(
    /** This options is relevant only if you do inplace checks and if you use the rebase option of the update_method. It
      * will automatically squash your commits beginning by squash!, fixup! or amend!, just like the option with the
      * same name when doing a git rebase.
  Default: True
      */
    autosquash: Option[ToJson /*bool*/] = None,
    /** Template to use as the commit message when using the merge or squash merge method. Template can also be defined
      * in the pull request body (see Defining the Commit Message). This option has been moved under the Queue Rules
      * section of the configuration and will be removed from this section in the future.
      */
    commitMessageTemplate: String = "",
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to merge pull request. If no merge_bot_account
      * is set, Mergify will merge the pull request itself. The user account must have already been logged in Mergify
      * dashboard once and have write or maintain permission. This option overrides the value defined in the Queue Rules
      * section of the configuration.
      */
    mergeBotAccount: String = "",
    /** Merge method to use. Possible values are merge, squash, rebase or fast-forward. fast-forward is not supported on
      * queues with speculative_checks > 1, batch_size > 1, or with allow_inplace_checks set to false. This option
      * overrides the value defined in the Queue Rules section of the configuration.
      */
    method: String = "",
    /** The name of the queue where the pull request should be added. If no name is set, routing_conditions will be
      * applied instead.
      */
    name: String = "",
    /** Allow merging Mergify configuration change.
  Default: false
      */
    allowMergingConfigurationChange: Option[ToJson /*bool*/] = None,
    /** Premium Plan Feature 🦾 Deprecated 😵 To set your priorities, you should now use Priority Rules. This sets the
      * priority of the pull request in the queue. The pull request with the highest priority is merged first. low,
      * medium, high are aliases for 1000, 2000, 3000.
  Default: medium
      */
    priority: Option[ToJson /*1 <= integer <= 10000 or low or medium or high*/] = None,
    /** Whether branch protections are required for queueing pull requests.
  Default: true
      */
    requireBranchProtection: Option[ToJson /*bool*/] = None,
    /** Essential Plan Feature 💪 For certain actions, such as rebasing branches, Mergify has to impersonate a GitHub
      * user. You can specify the account to use with this option. If no update_bot_account is set, Mergify picks
      * randomly one of the organization users instead. The user account must have already been logged in Mergify
      * dashboard once. This option overrides the value defined in the Queue Rules section of the configuration.
      */
    updateBotAccount: String = "",
    /** merge for all merge methods except fast-forward where rebase is used Method to use to update the pull request
      * with its base branch when the speculative check is done in-place. Possible values: * merge to merge the base
      * branch into the pull request. * rebase to rebase the pull request against its base branch. Note that the rebase
      * method has some drawbacks, see Using Rebase to Update. This option overrides the value defined in the Queue
      * Rules section of the configuration.
      */
    updateMethod: String = ""
  ) extends Action


  /** The rebase action will rebase the pull request against its base branch. To this effect, it clones the branch, run
    * git rebase locally and push back the result to the GitHub repository.
    */
  case class Rebase(
    /** When set to True, commits starting with fixup!, squash! and amend! are squashed during the rebase.
  Default: True
      */
    autosquash: Option[ToJson /*bool*/] = None,
    /** Essential Plan Feature 💪 For certain actions, such as rebasing branches, Mergify has to impersonate a GitHub
      * user. You can specify the account to use with this option. If no bot_account is set, Mergify picks randomly one
      * of the organization users instead. The user account must have already been logged in Mergify dashboard once.
      */
    botAccount: String = ""
  ) extends Action


  /** Note GitHub does not allow to request more than 15 users or teams for a review.
    */
  case class RequestReviews(
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to request a review on a pull request. If no
      * bot_account is set, Mergify will request the review itself.
      */
    botAccount: String = "",
    /** The username to request reviews from.
      */
    users: ToJson /*list of string or dictionary of login and weight*/,
    /** The team names to get the list of users to request reviews from.
      */
    usersFromTeams: ToJson /*list of string or dictionary of login and weight*/,
    /** Premium Plan Feature 🦾 Essential Plan Feature 💪 Pick random users and teams from the provided lists. When
      * random_count is specified, users and teams can be a dictionary where the key is the login and the value is the
      * weight to use. Weight must be between 1 and 65535 included.
      */
    randomCount: ToJson /*integer between 1 and 15*/,
    /** The team name to request reviews from.
      */
    teams: ToJson /*list of string or dictionary of login and weight*/
  ) extends Action


  /** The review action reviews the pull request. You can use it to approve or request a change on a pull request.
    */
  case class Review(
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to review a pull request. If no bot_account is
      * set, Mergify will review the pull request itself.
      */
    botAccount: String = "",
    /** The message to write as a comment.
      */
    message: String = "",
    /** The kind of review, can be APPROVE, REQUEST_CHANGES, COMMENT
      */
    `type`: String = "APPROVE"
  ) extends Action


  /** The squash action transforms pull request's n-commits into a single commit.
    */
  case class Squash(
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to squash a pull request. If no bot_account is
      * set, Mergify will squash the pull request itself.
      */
    botAccount: String = "",
    /** Defines what commit message to use for the squashed commit if not commit message is defined in the pull request
      * body (see Defining the Commit Message). Possible values are: all-commits to use the same format as GitHub
      * squashed merge commit. first-commit to use the message of the first commit of the pull request. title+body means
      * to use the title and body from the pull request itself as the commit message. The pull request number will be
      * added to end of the title.
      */
    commitMessage: String = "all-commits"
  ) extends Action


  /** The update action updates the pull request against its base branch. It works by merging the base branch into the
    * head branch of the pull request.
    */
  case class Update(
    /** Essential Plan Feature 💪 Mergify can impersonate a GitHub user to update a pull request. If no bot_account is
      * set, Mergify will update the pull request itself.
      */
    botAccount: String = ""
  ) extends Action}

package group7.daw.common

import group7.daw.comments.data.CommentRepository
import group7.daw.comments.models.Comment
import group7.daw.common.exceptions.*
import group7.daw.issue.data.IssueRepository
import group7.daw.issue.models.Issue
import group7.daw.project.data.ProjectRepository
import group7.daw.project.models.Project
import group7.daw.user.data.UserRepository
import group7.daw.user.models.User
import java.security.MessageDigest

class Utils(
        private val projectRepository: ProjectRepository,
        private val userRepository: UserRepository,
        private val issueRepository: IssueRepository,
        private val commentRepository: CommentRepository) {

    /**
     * helper function to validate project name (has to be unique)
     */
    fun getUserIdByUsername(username: String) : Int {
        return userRepository.checkIfUserExists(username).get().user_id
    }

    /**
     * helper function to validate project name (has to be unique)
     */
    fun validateProjectName(project_name: String) {

        val db = projectRepository.getProjectList().map { it.name }

        if (db.contains(project_name))
            throw ExceptionsProject.InvalidProjectName()
    }

    /**
     * helper function to turn a string representing an array of strings to a list of strings
     */
    fun stringToList(input: String): List<String> {
        return input
                .substring(1, input.length - 1) //remove the []
                .split(",")
    }

    /**
     * helper function to get the correct indexes depending on the page requested by the user
     */
    fun <T> getIndexes(list: List<T>, pageIndex: Int, pageSize: Int): List<Int> {
        val beggining: Int = if (pageSize * (pageIndex - 1) > list.size) {
            1
        } else pageSize * (pageIndex - 1)

        val end: Int = if (pageSize * (pageIndex) > list.size) {
            list.size
        } else pageSize * pageIndex

        return listOf(beggining, end)
    }

    /**
     * The request has the username input in the request and parsed by the Filter, which checks the password.
     * In order to guaranteee that the id of the non idempotent request matches the authenticated user, we
     * check the database for the credentials associated with the username (usernames are unique) and
     * see if the ID's match
     */
    fun validateUser(user: User) {

        val userFromDB = userRepository.checkIfUserExists(user.username)

        if (userFromDB.isEmpty)
            throw ExceptionsUser.NoSuchUserException()

        if (userFromDB.get().user_id != user.user_id)
            throw ExceptionsUser.InvalidUrlException()
    }

    /**
     * user validation for services where there is no need for the user account info
     */
    fun checkIfUserExists(user_id: Int){

        val user = userRepository.getUserAccountInfo(user_id)

        if (user.isEmpty)
            throw ExceptionsUser.NoSuchUserException()
    }

    /**
     * Checks to see if the project exists in the database and it belongs to that user
     */
    fun validateProjectID(project_id: Int, user_id: Int){

        val projectFromDB = checkIfProjectExists(project_id)

        if(projectFromDB.user_id != user_id)
            throw ExceptionsProject.MismatchedUserException()

    }

    fun checkIfProjectExists(project_id: Int): Project{

        val projectFromDB = projectRepository.getProject(project_id)

        if (projectFromDB.isEmpty)
            throw ExceptionsProject.NoProjectFoundException()

        return projectFromDB.get()
    }

    /**
     * checks to see if issue exists in the database and if it belongs to the project
     */
    fun validateIssueID(issue_id:Int,project_id: Int){

        val issue = checkIfIssueExists(issue_id)

        if(issue.project_id != project_id)
            throw InvalidProjectForIssueException()
    }

    fun checkIfIssueExists(issue_id:Int): Issue {
        val issue = issueRepository.getIssue(issue_id)

        if (issue.isEmpty)
            throw NoSuchIssueException()

        return issue.get()
    }

    /**
     * checks the issue owner for delete/edit operations
     */
    fun validateIssueOwner(issue_id:Int, user_id: Int){

        val issue = checkIfIssueExists(issue_id)

        if(issue.user_id != user_id)
            throw InvalidUserForIssueException()

    }

    /**
     * checks to see if comment exists in the database and if it belongs to the issue
     */
    fun validateCommentID(comment_id: Int, issue_id:Int){

        val comment = checkIfCommentExists(comment_id)

        if(comment.issue_id != issue_id)
            throw ExceptionsComment.MismatchedIssueCommentException()
    }

    fun checkIfCommentExists(comment_id: Int): Comment {

        val comment = commentRepository.getComment(comment_id)

        if (comment.isEmpty)
            throw ExceptionsComment.CommentNotFoundException()

        return comment.get()
    }

    fun validateCommentOwner(comment_id: Int,  user_id: Int){

        val comment = checkIfCommentExists(comment_id)

        if(comment.user_id != user_id)
            throw ExceptionsComment.MismatchedUserCommentException()
    }

    // password safety
    fun hashString(input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-512")
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}
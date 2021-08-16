package group7.daw.common.pipeline

import group7.daw.common.Utils
import group7.daw.user.models.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//User: 3
//http://localhost:8080/group7api/users/2 (get, edit, delete)

//project 4 and 3 for create
//http://localhost:8080/group7api/4/projects/4 (edit, delete) -> check if project belongs to that user

//issue 6 and 5 for create
//http://localhost:8080/group7api/2/projects/16/issues/4 (edit, delte) -> check project belongs to user and issue belongs to project

//comment 8 and 7 for create
//http://localhost:8080/group7api/2/projects/4/issues/2/comments/1 -> check project belongs to user, issue belongs to project, comment belongs to issue

@Component
class AccessControlInterceptor(private val utils: Utils) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(AccessControlInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {


        super.preHandle(request, response, handler)

        val uri = request.requestURI

        if (uri == "/group7api" || uri == "/group7api/users" || uri == "/group7api/home" || uri== "/group7api/users/login")
            return true

        val partsOfUrl = request.requestURI.subSequence(1, request.requestURI.length).split("/")

        when (partsOfUrl.size) {

            //user related operations
            3 -> {

                //project creation is /group7api/userid/projects which is unfortunate
                val userId: Int = if (uri.contains("projects")) {
                    Integer.parseInt(partsOfUrl[1])
                } else {
                    Integer.parseInt(partsOfUrl[2])
                }

                //see if user exists on all requests
                utils.checkIfUserExists(userId)

                //see if the id on the url matches the user id in the database
                if (request.method != "GET")
                    utils.validateUser(User(userId, request.getAttribute(USER_USERNAME_KEY) as String, null))

                logger.info("Access Control Interceptor - Access allowed.")

            }

            // project related operations
            4 -> {

                val userId = Integer.parseInt(partsOfUrl[1])
                val projectId = Integer.parseInt(partsOfUrl[3])

                utils.checkIfUserExists(userId)
                utils.validateProjectID(projectId, userId)

                //see if the id on the url matches the user id in the database
                if (request.method != "GET") {
                    utils.validateUser(User(userId, request.getAttribute(USER_USERNAME_KEY) as String, null))

                }
            }

            //issue creation
            5 -> {
                val userId = Integer.parseInt(partsOfUrl[1])
                utils.checkIfUserExists(userId)
                logger.info("Access Control Interceptor - Access allowed.")
            }

            // issue related operations
            6 -> {

                val userId = Integer.parseInt(partsOfUrl[1])
                val projectId = Integer.parseInt(partsOfUrl[3])
                val issueId = Integer.parseInt(partsOfUrl[5])

                utils.checkIfUserExists(userId)
                utils.validateIssueID(issueId, projectId)

                // anyone can create issues on another project, they just can't change or delete them
                if (request.method != "GET")
                    utils.validateIssueOwner(
                        issueId,
                        utils.getUserIdByUsername(request.getAttribute(USER_USERNAME_KEY) as String)
                    )

                logger.info("Access Control Interceptor - Access allowed.")
            }

            //comment creation,label and state operations
            7 -> {

                logger.info("Access Control Interceptor.")

                val userId = Integer.parseInt(partsOfUrl[1])
                val projectId = Integer.parseInt(partsOfUrl[3])
                val issueId = Integer.parseInt(partsOfUrl[5])

                utils.checkIfUserExists(userId)
                utils.validateIssueID(issueId, projectId)

                //anyone can create issues on another project, they just can't change or delete them
                if (request.method != "GET" && !request.requestURI.contains("comments"))
                    utils.validateIssueOwner(
                        issueId,
                        utils.getUserIdByUsername(request.getAttribute(USER_USERNAME_KEY) as String)
                    )
            }

            // comment related operations
            8 -> {

                logger.info("Access Controll Interceptor.")

                val userId = Integer.parseInt(partsOfUrl[1])
                val projectId = Integer.parseInt(partsOfUrl[3])
                val issueId = Integer.parseInt(partsOfUrl[5])
                val commentId = Integer.parseInt(partsOfUrl[7])

                utils.checkIfUserExists(userId)
                utils.validateProjectID(projectId, userId)
                utils.validateIssueID(issueId, projectId)
                utils.validateCommentID(commentId, issueId)

                if (request.method != "GET")
                    utils.validateCommentOwner(
                        commentId,
                        utils.getUserIdByUsername(request.getAttribute(USER_USERNAME_KEY) as String)
                    )
            }

        }
        return true
    }
}
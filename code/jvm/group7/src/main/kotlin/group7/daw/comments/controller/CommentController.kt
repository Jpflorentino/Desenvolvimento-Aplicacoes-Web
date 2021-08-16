package group7.daw.comments.controller

import group7.daw.EmbeddedEntity
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.comments.models.*
import group7.daw.comments.service.CommentService
import group7.daw.common.Utils
import group7.daw.common.exceptions.InvalidQueryStringException
import group7.daw.common.pipeline.USER_USERNAME_KEY
import group7.daw.project.models.Project
import group7.daw.selfLink
import group7.daw.user.models.UserOutputModel
import group7.daw.user.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest
import kotlin.math.ceil

@RestController
@RequestMapping("group7api/{user_id}/projects/{project_id}/issues/{issue_id}/comments")
class CommentController(private val commentService: CommentService, private val userService: UserService, private val utils: Utils) {

    @GetMapping()
    fun getCommentListHandler(
            request: HttpServletRequest,
            @PathVariable issue_id: Int,
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            ): SirenEntity<CommentListOutputModel>{

        val pageIndex: Int
        val pageSize: Int
        val comments: List<Comment>
        val indexes: List<Int>
        val page: List<Comment>

        try {
            val queryStringOptions = request.queryString.split("&")
            pageIndex = Integer.parseInt(queryStringOptions[0].split("=")[1])
            pageSize = Integer.parseInt(queryStringOptions[1].split("=")[1])
            comments = commentService.getCommentList(issue_id, pageIndex)
            indexes = utils.getIndexes(comments, pageIndex, pageSize)
            page = comments.subList(indexes[0], indexes[1])
        } catch (e: Exception) {
            throw InvalidQueryStringException()
        }

        val totalPages = ceil(comments.size.toDouble() / pageSize.toDouble())

        val previousPageIndex: Int = if (pageIndex == 1) {
            1
        } else pageIndex - 1

        val subs = page.map {
            val user = userService.getUserAccountInfo(it.user_id)
            EmbeddedEntity(
                    listOf("comment"),
                    listOf("item"),
                    it,
                    entities = listOf(
                            EmbeddedEntity(
                                    listOf("user"),
                                    listOf("owner"),
                                    UserOutputModel(user.user_id,user.username),
                                    null,
                                    null,
                                    listOf(selfLink("/group7api/users/${user.user_id}/"))

                            )
                    ),
                    null,
                    listOf(selfLink("/group7api/${user_id}/projects/${project_id}/issues/${it.issue_id}/comments/${it.comment_id}"))
            )
            }

        return CommentListOutputModel(comments.size, totalPages.toInt(), pageIndex, pageSize, issue_id).toSirenObject(
                entities = subs,
                actions = listOf(createCommentAction(user_id,project_id, issue_id)),
                links = listOf(
                        selfLink("/group7api/${user_id}/projects/${project_id}/issues?PageIndex={index}&PageSize={pageSize}"),
                        SirenLink(
                                listOf("previous"),
                                "/group7api/${user_id}/projects/${project_id}/issues?pageIndex=${previousPageIndex}&pageSize=$pageSize"
                        ),
                        SirenLink(
                                listOf("next"),
                                "/group7api/${user_id}/projects/${project_id}/issues?pageIndex=${pageIndex + 1}&pageSize=$pageSize"
                        ),
                        SirenLink(
                            listOf("owner"),
                            "/group7api/${user_id}/projects/${project_id}/issues/${issue_id}"
                        )
                )
        )
    }

    @GetMapping("/{comment_id}")
    fun getCommentHandler(
            @PathVariable issue_id: Int,
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @PathVariable comment_id: Int
    ): SirenEntity<CommentOutputModel>{

        val comment = commentService.getComment(comment_id)

        val user = userService.getUserAccountInfo(comment.user_id)

        return CommentOutputModel(
                comment.user_id,
                comment.issue_id,
                comment.comment_id!!,
                comment.comment_text,
                comment.creation_date
                )
                .toSirenObject(
                        entities = listOf(
                                EmbeddedEntity(
                                        clazz = listOf("user"),
                                        rel = listOf("owner"),
                                        properties = user, //props
                                        links = listOf(selfLink("/group7api/user/${user.user_id}"))
                                )
                        ), links = listOf(
                        selfLink("/group7api/$user_id/projects/${project_id}/issues/$issue_id/comments/$comment_id"),
                        SirenLink(rel = listOf("owner"), href = "/group7api/$user_id/projects/${project_id}/issues/$issue_id"),
                        SirenLink(rel = listOf("comment","collection"), href = "/group7api/$user_id/projects/${project_id}/issues/$issue_id/comments?PageIndex={index}&PageSize={pageSize}")
                ), actions = listOf(
                       editComment(user_id,project_id,issue_id,comment_id),
                       deleteComment(user_id,project_id,issue_id,comment_id)
                ))

    }

    @PostMapping() //create comment
    fun createCommentHandler(
            @PathVariable issue_id: Int,
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @RequestBody commentInputModel: CommentInputModel,
            request: HttpServletRequest
    ): ResponseEntity<URI>{

        val comment_id = commentService.createComment(commentInputModel, issue_id, request.getAttribute(USER_USERNAME_KEY) as String)

        return ResponseEntity.created(URI.create("/group7api/user/$user_id/projects/${project_id}/issues/$issue_id/comments/$comment_id")).build()
    }

    @DeleteMapping("/{comment_id}")
    fun deleteCommentHandler(
            @PathVariable issue_id: Int,
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @PathVariable comment_id: Int
    ): ResponseEntity<URI>{

        commentService.deleteComment(comment_id)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{comment_id}")
    fun editCommentHandler(
            @PathVariable issue_id: Int,
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @PathVariable comment_id: Int,
            @RequestBody commentInputModel: CommentInputModel
    ): ResponseEntity<URI>{

        commentService.editComment(comment_id, commentInputModel)

        return ResponseEntity.noContent().build()
    }

}
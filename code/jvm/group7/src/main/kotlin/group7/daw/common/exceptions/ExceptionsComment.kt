package group7.daw.common.exceptions

import group7.daw.ErrorClassJSON
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionsComment {

    /**
     * exception for invalid comment id
     */
    class CommentNotFoundException(): Exception()

    /**
     * mismatched issue id for comment
     */
    class MismatchedIssueCommentException(): Exception()

    class MismatchedUserCommentException(): Exception()

    @ExceptionHandler(CommentNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun returnErrorNoCommentFound(httpRequest: HttpServletRequest) = ResponseEntity
            .status(404)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Comments",
                            title = "Comment not found",
                            detail = "A comment with that id does not exist",
                            status = 404
                    )
            )

    @ExceptionHandler(MismatchedIssueCommentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun returnMismatchedIssueComment(httpRequest: HttpServletRequest) = ResponseEntity
            .status(400)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Comments",
                            title = "Mismatched issue id",
                            detail = "A comment with that id does not belong to an issue with that id",
                            status = 400
                    )
            )

    @ExceptionHandler(MismatchedUserCommentException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun returnMismatchedUserComment(httpRequest: HttpServletRequest) = ResponseEntity
            .status(403)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Comments",
                            title = "Mismatched user id",
                            detail = "You can't edit or delete other users' comments",
                            status = 403
                    )
            )


}
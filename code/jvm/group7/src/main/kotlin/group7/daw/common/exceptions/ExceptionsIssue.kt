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

/**
 * exception used for uncreated project
 */
class NoSuchIssueException() : Exception()

class InvalidNewStateException(): Exception()

class InvalidNewLabelException(): Exception()

class NoLabelException(): Exception()

class InvalidProjectForIssueException(): Exception()

class InvalidUserForIssueException(): Exception()

@ControllerAdvice
class ExceptionsIssue {

    @ExceptionHandler(NoSuchIssueException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun returnErrorNoIssueFound(httpRequest: HttpServletRequest) = ResponseEntity
            .status(404)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Issues",
                            title = "Issue not found",
                            detail = "An issue with that id does not exist",
                            status = 404
                    )
            )

    @ExceptionHandler(NoLabelException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun returnNoLabel(httpRequest: HttpServletRequest) = ResponseEntity
            .status(404)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Issues",
                            title = "Label not found",
                            detail = "The label does not exist in the issue",
                            status = 404
                    )
            )

    @ExceptionHandler(InvalidNewStateException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun returnInvalidNewState(httpRequest: HttpServletRequest) = ResponseEntity
            .status(403)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Issues",
                            title = "Invalid new state",
                            detail = "The new state has to belong to the allowed project states and the allowed transitions",
                            status = 403
                    )
            )

    @ExceptionHandler(InvalidNewLabelException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun returnInvalidNewLabel(httpRequest: HttpServletRequest) = ResponseEntity
            .status(403)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Issues",
                            title = "Invalid new label",
                            detail = "The new label has to belong to the allowed project labels",
                            status = 403
                    )
            )

    @ExceptionHandler(InvalidProjectForIssueException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun returnInvalidProjectForIssue(httpRequest: HttpServletRequest) = ResponseEntity
            .status(400)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Issues",
                            title = "Invalid project for issue",
                            detail = "The issue with that id doesn't belong to the project with that id",
                            status = 400
                    )
            )

    @ExceptionHandler(InvalidUserForIssueException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun returnInvalidUserForIssue(httpRequest: HttpServletRequest) = ResponseEntity
            .status(403)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Issues",
                            title = "Invalid User for issue",
                            detail = "You can't edit or delete other's issues",
                            status = 401
                    )
            )
}
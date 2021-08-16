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
class ExceptionsProject {
    /**
     * Since we use the user service to get the user details, and that
     * method throws this controller's handlers, we need a 404 for user and project
     */
    class NoProjectFoundException() : Exception()

    /**
     * exception used for when user tries to create/edit a project that doesn't belong to his account
     */
    class InvalidUserException() : Exception()

    /**
     * exception for a bad request where the project has the wrong user
     */
    class MismatchedUserException() : Exception()

    /**
     * exception used for when user doesn't send the necessary fields for project creation
     */
    class InvalidFieldsException() : Exception()

    /**
     * exception used for conflicting project names (has to be unique)
     */
    class InvalidProjectName() : Exception()

    @ExceptionHandler(NoProjectFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun returnErrorNoProjectFound(httpRequest: HttpServletRequest) = ResponseEntity
        .status(404)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects",
                title = "Project not found",
                detail = "A project with that id does not exist",
                status = 404
            )
        )

    @ExceptionHandler(InvalidUserException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun returnInvalidUser(httpRequest: HttpServletRequest) = ResponseEntity
        .status(403)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects",
                title = "Invalid User",
                detail = "You cannot create projects with another user as an owner",
                status = 403
            )
        )

    @ExceptionHandler(MismatchedUserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun returnMismatchedUser(httpRequest: HttpServletRequest) = ResponseEntity
            .status(400)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                        type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects",
                        title = "Invalid User",
                        detail = "That project does not belong to that user",
                        status = 400
                    )
            )

    @ExceptionHandler(InvalidFieldsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun returnInvalidFields(httpRequest: HttpServletRequest) = ResponseEntity
        .status(400)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects",
                title = "Invalid Fields",
                detail = "Invalid fields for project creation",
                status = 400
            )
        )

    @ExceptionHandler(InvalidProjectName::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    fun returnInvalidProjectName(httpRequest: HttpServletRequest) = ResponseEntity
        .status(409)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects",
                title = "Invalid Project Name",
                detail = "Project with that name already exists",
                status = 409
            )
        )
}
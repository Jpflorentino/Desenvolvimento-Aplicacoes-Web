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
class ExceptionsUser {

    class UsernameAlreadyInUseException() : Exception()
    class InvalidUrlException() : Exception()
    class NoSuchUserException() : Exception()
    class InvalidCredentialsException(): Exception()

    @ExceptionHandler(InvalidCredentialsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    fun returnErrorInvalidCredentials(httpRequest: HttpServletRequest) = ResponseEntity
        .status(401)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Users/User.md",
                title = "Invalid Credentials",
                detail = "Invalid Credentials",
                status = 401
            )
        )

    @ExceptionHandler(NoSuchUserException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun returnErrorNoUserFound(httpRequest: HttpServletRequest) = ResponseEntity
        .status(404)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Users/User.md",
                title = "User not found",
                detail = "A user with that id does not exist",
                status = 404
            )
        )

    @ExceptionHandler(InvalidUrlException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    fun returnInvalidURL(httpRequest: HttpServletRequest) = ResponseEntity
        .status(403)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Users/User.md",
                title = "Invalid URL",
                detail = "You can't edit or delete other accounts",
                status = 403
            )
        )

    @ExceptionHandler(UsernameAlreadyInUseException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    fun returnUsernameAlreadyInUse(httpRequest: HttpServletRequest) = ResponseEntity
        .status(409)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            ErrorClassJSON(
                type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Users/User.md",
                title = "Username already in use",
                detail = "Username conflict - you have to choose other username",
                status = 409
            )
        )
}
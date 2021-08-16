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

class InvalidPageException() : Exception()

class InvalidQueryStringException(): Exception()

@ControllerAdvice
class ExceptionsHome {

    @ExceptionHandler(InvalidPageException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun returnInvalidPageException(httpRequest: HttpServletRequest) = ResponseEntity
            .status(400)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Home/Home.md",
                            title = "Invalid Page",
                            detail = "Result pages start at 1",
                            status = 400
                    )
            )

    @ExceptionHandler(InvalidQueryStringException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun returnInvalidQueryStringException(httpRequest: HttpServletRequest) = ResponseEntity
            .status(400)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                    ErrorClassJSON(
                            type = "https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Home/Home.md",
                            title = "No Query String",
                            detail = "All requests for lists need a valid query string with the parameters PageIndex and PageSize",
                            status = 400
                    )
            )
}
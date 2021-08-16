package group7.daw.user.controllers

import group7.daw.*
import group7.daw.common.pipeline.USER_USERNAME_KEY
import group7.daw.user.models.*
import group7.daw.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

private val logger = LoggerFactory.getLogger(Group7Application::class.java)

@RestController
@RequestMapping("group7api/users", produces = [SIREN_MEDIA_TYPE, MediaType.APPLICATION_JSON_VALUE])
class UserController(private val userService: UserService) {

    @PostMapping() // Create User
    fun createUserAccount(
        @RequestBody userCredentials: UserInputModel,
    ): ResponseEntity<URI> {
        val user = User(-1, userCredentials.username, userCredentials.password)
        val id = userService.createUser(user)
        return ResponseEntity.created(URI.create("/group7api/users/$id")).build()
    }

    @PostMapping("/login") //login: has to be a post because GET requests cannot have a body according to the docs
    fun loginUser(
       @RequestBody userCredentials: UserInputModel
    ): SirenEntity<UserOutputModel> {
        val user = User(-1, userCredentials.username, userCredentials.password)
        val id = userService.loginUser(user)
        return UserOutputModel(id, user.username).toSirenObject(
            links = listOf(
                selfLink("/group7api/users/${id}"),
                SirenLink(
                    listOf("user", "projects"),
                    "/group7api/${id}/projects?PageIndex={index}&PageSize={pageSize}"
                )
            ),
            actions = listOf(
                setUserActionEdit(id),
                setUserActionDelete(id)
            )
        )
    }

    @GetMapping("/{user_id}") // Get user info
    fun getUserAccountInformation(@PathVariable user_id: Int): SirenEntity<UserOutputModel> {
        val user = userService.getUserAccountInfo(user_id)
        return UserOutputModel(user.user_id, user.username).toSirenObject(
            links = listOf(
                selfLink("/group7api/users/$user_id"),
                SirenLink(
                    listOf("user", "projects"),
                    "/group7api/$user_id/projects?PageIndex={index}&PageSize={pageSize}"
                )
            ),
            actions = listOf(
                setUserActionEdit(user.user_id),
                setUserActionDelete(user.user_id)
            )
        )
    }

    @PutMapping("/{user_id}") // Edit user
    fun editUserAccount(
        @PathVariable user_id: Int,
        @RequestBody body: EditUserInputModel,
        httpRequest: HttpServletRequest
    ) : SirenEntity<UserOutputModel> {

        val user = User(
            user_id,
            httpRequest.getAttribute(USER_USERNAME_KEY) as String,
            ""
        )

        val editedUser = userService.editUserAccount(user, body.newPassword)

        return UserOutputModel(editedUser.user_id, editedUser.username).toSirenObject(
            links = listOf(
                selfLink("/group7api/users/$user_id"),
                SirenLink(
                    listOf("user", "projects"),
                    "/group7api/$user_id/projects"
                )
            ),
            actions = listOf(
                setUserActionEdit(editedUser.user_id),
                setUserActionDelete(editedUser.user_id)
            )
        )
    }

    @DeleteMapping("/{user_id}") // Delete user
    fun deleteUserAccount(
        @PathVariable user_id: Int,
        httpRequest: HttpServletRequest
    ): ResponseEntity<URI> {

        val user = User(
            user_id,
            httpRequest.getAttribute(USER_USERNAME_KEY) as String,
            ""
        )

        userService.deleteUserAccount(user)

        return ResponseEntity.noContent().build()
    }
}
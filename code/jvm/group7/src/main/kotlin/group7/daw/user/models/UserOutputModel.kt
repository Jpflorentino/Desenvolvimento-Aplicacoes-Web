package group7.daw.user.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class UserOutputModel(
    var user_id: Int,
    var username: String,
)

fun UserOutputModel.toSirenObject(links: List<SirenLink>, actions: List<SirenAction>? = null) = SirenEntity(
            properties = this,
            clazz = listOf("user"),
            links = links,
            actions = actions
    )

fun setUserActionEdit(user_id: Int) : SirenAction {
    return SirenAction(
        name = "edit-user-account",
        title = "Edit user account",
        href = "/group7api/users/$user_id",
        method = HttpMethod.PUT,
        type = MediaType.APPLICATION_JSON,
        fields = listOf(SirenAction.Field("newPassword", "text"))
    )
}

fun setUserActionDelete(user_id: Int) : SirenAction {
    return SirenAction(
        name = "delete-item",
        title = "Delete user account",
        href = "/group7api/users/$user_id",
        method = HttpMethod.DELETE,
        type = MediaType.APPLICATION_JSON
    )
}

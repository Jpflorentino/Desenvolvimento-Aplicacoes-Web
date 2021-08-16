package group7.daw.comments.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.SubEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class CommentOutputModel(
        val user_id: Int,
        val issue_id: Int,
        val comment_id:Int,
        val comment_text: String,
        val creation_date:Int
)

fun CommentOutputModel.toSirenObject(links: List<SirenLink>, actions: List<SirenAction>? = null, entities: List<SubEntity>) = SirenEntity(
        properties = this,
        clazz = listOf("comment"),
        links = links,
        actions = actions,
        entities = entities
)

fun editComment(user_id: Int, project_id: Int, issue_id: Int, comment_id: Int): SirenAction {
    return SirenAction(
            name = "edit-comment",
            title = "Edit comment",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id/comments/$comment_id",
            method = HttpMethod.PUT,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(
                    SirenAction.Field("comment_text", "String"),
            )
    )
}

fun deleteComment(user_id: Int, project_id: Int, issue_id: Int,comment_id: Int): SirenAction {
    return SirenAction(
            name = "delete-comment",
            title = "Delete comment",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id/comments/$comment_id",
            method = HttpMethod.DELETE
    )
}


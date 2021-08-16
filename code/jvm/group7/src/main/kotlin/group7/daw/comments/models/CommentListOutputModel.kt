package group7.daw.comments.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.SubEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class CommentListOutputModel(
        val collectionSize: Int,
        val totalPages: Int,
        val pageIndex: Int,
        val pageSize: Int,
        val issue_id: Int
)

fun CommentListOutputModel.toSirenObject(
        links: List<SirenLink>,
        actions: List<SirenAction>? = null,
        entities: List<SubEntity>
) = SirenEntity(
        clazz = listOf("comment", "collection"),
        properties = this,
        entities = entities,
        actions = actions,
        links = links
)

fun createCommentAction(user_id: Int, project_id: Int, issue_id: Int): SirenAction {
    return SirenAction(
            name = "create-comment",
            title = "Create comment",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id/comments",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(
                    SirenAction.Field("comment_text", "text"),
            )
    )
}
package group7.daw.issue.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.SubEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class IssueListOutputModel(
        val collectionSize: Int,
        val totalPages: Int,
        val pageIndex: Int,
        val pageSize: Int,
        val project_id: Int
)

fun IssueListOutputModel.toSirenObject(
        links: List<SirenLink>,
        actions: List<SirenAction>? = null,
        entities: List<SubEntity>
) = SirenEntity(
        clazz = listOf("issue", "collection"),
        properties = this,
        entities = entities,
        actions = actions,
        links = links
)

fun createIssueAction(user_id: Int, project_id: Int): SirenAction {
    return SirenAction(
            name = "create-issue",
            title = "Create issue",
            href = "/group7api/$user_id/projects/$project_id/issues",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(
                    SirenAction.Field("name", "text"),
                    SirenAction.Field("description", "text"),
                    SirenAction.Field("closed_on", "number"),
            )
    )
}
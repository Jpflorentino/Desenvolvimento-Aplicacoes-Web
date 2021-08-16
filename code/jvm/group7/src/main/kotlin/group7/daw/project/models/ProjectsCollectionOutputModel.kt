package group7.daw.project.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.SubEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class ProjectsCollectionOutputModel(
    val collectionSize: Int,
    val totalPages: Int,
    val pageIndex: Int,
    val pageSize: Int,
    val user_id: Int?
)

fun ProjectsCollectionOutputModel.toSirenObject(
    links: List<SirenLink>,
    actions: List<SirenAction>? = null,
    entities: List<SubEntity>
) = SirenEntity(
    clazz = listOf("project", "collection"),
    properties = this,
    entities = entities,
    actions = actions,
    links = links
)

fun createProjectAction(user_id: Int): SirenAction {
    return SirenAction(
        name = "create-project",
        title = "Create project",
        href = "/group7api/$user_id/projects/",
        method = HttpMethod.POST,
        type = MediaType.APPLICATION_JSON,
        fields = listOf(
            SirenAction.Field("name", "text"),
            SirenAction.Field("description", "text"),
            SirenAction.Field("allowed_labels_set", "text"),
            SirenAction.Field("allowed_states_set", "text"),
            SirenAction.Field("allowed_transitions_set", "text"),
        )
    )
}
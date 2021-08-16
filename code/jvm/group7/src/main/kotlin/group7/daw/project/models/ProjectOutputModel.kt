package group7.daw.project.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.SubEntity
import group7.daw.project.data.Transition
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class ProjectOutputModel(
        val name: String,
        val description: String,
        val project_id: Int,
        val labels: List<String>,
        val states: List<String>,
        val transitions: List<Transition>
)

fun ProjectOutputModel.toSirenObject(links: List<SirenLink>, actions: List<SirenAction>? = null, entities: List<SubEntity>) = SirenEntity(
        properties = this,
        clazz = listOf("project"),
        links = links,
        actions = actions,
        entities = entities
)

fun editProjectAction(user_id: Int, project_id: Int) : SirenAction {
    return SirenAction(
            name = "edit-project",
            title = "Edit project",
            href = "/group7api/$user_id/projects/$project_id",
            method = HttpMethod.PUT,
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

fun deleteProjectAction(user_id: Int, project_id: Int) : SirenAction {
    return SirenAction(
            name = "delete-project",
            title = "Delete project",
            href = "/group7api/$user_id/projects/$project_id",
            method = HttpMethod.DELETE,
            type = MediaType.APPLICATION_JSON
    )
}
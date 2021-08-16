package group7.daw.issue.models

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import group7.daw.SubEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class IssueOutputModel(
        val user_id: Int,
        val project_id: Int,
        val issue_id: Int,
        val name:String,
        val description: String,
        val labels: List<String>,
        val opened_on: Int,
        val closed_on: Int?,
        val state_name: String
)

fun IssueOutputModel.toSirenObject(links: List<SirenLink>, actions: List<SirenAction>? = null, entities: List<SubEntity>) = SirenEntity(
        properties = this,
        clazz = listOf("issue"),
        links = links,
        actions = actions,
        entities = entities
)

fun editIssue(user_id: Int, project_id: Int, issue_id: Int): SirenAction {
    return SirenAction(
            name = "edit-issue",
            title = "Edit issue",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id",
            method = HttpMethod.PUT,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(
                    SirenAction.Field("name", "text"),
                    SirenAction.Field("description", "text"),
                    SirenAction.Field("closed_on", "number"),
            )
    )
}

fun deleteIssue(user_id: Int, project_id: Int, issue_id: Int): SirenAction{
    return SirenAction(
            name = "delete-issue",
            title = "Delete issue",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id",
            method = HttpMethod.DELETE
    )
}

fun addLabel(user_id: Int, project_id: Int, issue_id: Int):SirenAction{
    return SirenAction(
            name = "add-label",
            title = "Add Label",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id/labels",
            method = HttpMethod.POST,
            fields = listOf(
                    SirenAction.Field("labels", "text"),
            )
    )
}

fun removeLabel(user_id: Int, project_id: Int, issue_id: Int):SirenAction{
    return SirenAction(
            name = "remove-label",
            title = "Remove Label",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id/labels",
            method = HttpMethod.DELETE,
            fields = listOf(
                    SirenAction.Field("labels", "text"),
            )
    )
}

fun changeState(user_id: Int, project_id: Int, issue_id: Int):SirenAction{
    return SirenAction(
            name = "change-state",
            title = "Remove Label",
            href = "/group7api/$user_id/projects/$project_id/issues/$issue_id/state",
            method = HttpMethod.PUT,
            fields = listOf(
                    SirenAction.Field("state", "text"),
            )
    )
}

//get issue collection





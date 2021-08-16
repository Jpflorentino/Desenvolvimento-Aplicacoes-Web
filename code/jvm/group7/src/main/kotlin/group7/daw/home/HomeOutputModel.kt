package group7.daw.home

import group7.daw.SirenAction
import group7.daw.SirenEntity
import group7.daw.SirenLink
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

data class HomeOutputModel(
        val variable: String? = null
)

data class APILinks(
        val home: String = "/group7api/home",
        val allProjects: String = "/group7api/projects?PageIndex={index}&PageSize={pageSize}",
        val userDetails: String = "/group7api/users/{userid}",
        val userLogin: String = "/group7api/users/login",
        val userProjects: String = "/group7api/{userid}/projects?PageIndex={index}&PageSize={pageSize}",
        val userIssues: String = "/group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}",
        val projectDetails: String = "/group7api/{userid}/projects/{projectid}",
        val issueDetails: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}",
        val issueCollection: String = "/group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}",
        val issueCommentsCollection: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/comments?PageIndex={index}&PageSize={pageSize}",
        val commentDetails: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/comments/{commentid}",
        val changeLabel: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/labels", //same URL for add and delete label
        val changeState: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/state"
)

fun HomeOutputModel.toSirenObject(links: List<SirenLink>, actions: List<SirenAction>? = null) = SirenEntity(
        properties = this,
        clazz = listOf("home"),
        links = links,
        actions = actions
)

fun createUserAction() : SirenAction {
    return SirenAction(
            name = "create-user-account",
            title = "Create user account",
            href = "/group7api/users/",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            fields = listOf(SirenAction.Field("username", "text"), SirenAction.Field("password", "text"))
    )
}
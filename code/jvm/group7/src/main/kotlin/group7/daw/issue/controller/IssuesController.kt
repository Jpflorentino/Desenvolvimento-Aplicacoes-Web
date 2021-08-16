package group7.daw.issue.controller

import group7.daw.*
import group7.daw.common.Utils
import group7.daw.common.exceptions.InvalidQueryStringException
import group7.daw.common.pipeline.USER_USERNAME_KEY
import group7.daw.issue.models.*
import group7.daw.issue.service.IssueService
import group7.daw.project.controller.ProjectsController
import group7.daw.project.models.Project
import group7.daw.project.models.ProjectsCollectionOutputModel
import group7.daw.project.models.createProjectAction
import group7.daw.project.models.toSirenObject
import group7.daw.user.models.UserOutputModel
import group7.daw.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.servlet.http.HttpServletRequest
import kotlin.math.ceil

val logger = LoggerFactory.getLogger(Group7Application::class.java)

@RestController
@RequestMapping("group7api/{user_id}/projects/{project_id}/issues")
class IssuesController(private val issueService: IssueService,
                       private val userService: UserService,
                       private val utils: Utils) {

    @GetMapping("/{issue_id}")
    fun getIssue(
            @PathVariable issue_id: Int,
            @PathVariable user_id: Int,
            @PathVariable project_id: Int
    ): SirenEntity<IssueOutputModel>{

        val issue = issueService.getIssue(issue_id)

        val user = userService.getUserAccountInfo(user_id)

        return IssueOutputModel(
                issue.user_id,
                issue.project_id,
                issue_id,
                issue.name,
                issue.description,
                issueService.getIssueLabels(issue_id),
                issue.opened_on,
                issue.closed_on,
                issue.state_name)
                .toSirenObject(
                        entities = listOf(
                                EmbeddedEntity(
                                        clazz = listOf("user"),
                                        rel = listOf("owner"),
                                        properties = user, //props
                                        links = listOf(selfLink("/group7api/user/$user_id"))
                                )
                        ), links = listOf(
                        selfLink("/group7api/user/$user_id/projects/${issue.project_id}/issues/$issue_id"),
                        SirenLink(rel = listOf("issues","collection"), href = "/group7api/$user_id/projects/${issue.project_id}/issues/$issue_id?PageIndex={index}&PageSize={pageSize}"),
                        SirenLink(rel = listOf("owner"), href = "/group7api/$user_id/projects/${issue.project_id}"),
                        SirenLink(rel = listOf("comments","collection"), href = "/group7api/$user_id/projects/${issue.project_id}/issues/$issue_id/comments?PageIndex={index}&PageSize={pageSize}")
                ), actions = listOf(
                        editIssue(user_id, issue.project_id, issue_id),
                        deleteIssue(user_id, issue.project_id, issue_id),
                        addLabel(user_id, issue.project_id, issue_id),
                        removeLabel(user_id, issue.project_id, issue_id),
                        changeState(user_id, issue.project_id, issue_id)
                ))

    }

    @PostMapping() // Create issue
    fun createIssue(
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @RequestBody issueInputModel: IssueInputModel,
            request: HttpServletRequest
    ): ResponseEntity<URI> {

        logger.info("Issues controller - Received a request to open an issue.")

        val createdDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toInt()

        val issueId = issueService.createIssue(issueInputModel, project_id, createdDate, request.getAttribute(USER_USERNAME_KEY) as String)

        return ResponseEntity.created(URI.create("/group7api/$user_id/projects/${project_id}/issues/${issueId}")).build()
    }

    @PutMapping("/{issue_id}") //edit issue
    fun editIssue(
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @PathVariable issue_id: Int,
            @RequestBody issueInputModel: IssueInputModel,
    ): ResponseEntity<URI> {

        issueService.editIssue(issueInputModel, issue_id)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{issue_id}") // Delete issue
    fun removeIssue(
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @PathVariable issue_id: Int
    ): ResponseEntity<URI>{

        issueService.deleteIssue(issue_id)

        return ResponseEntity.noContent().build()

    }

    @PostMapping("/{issue_id}/labels") /// Add label
    fun addLabel(
            @PathVariable user_id: Int,
            @PathVariable project_id: Int,
            @PathVariable issue_id: Int,
            @RequestBody labelInputModel: LabelInputModel)
    : ResponseEntity<URI>{

        issueService.addLabelToIssue(IssueHasLabel(project_id, issue_id, labelInputModel.label))

        return ResponseEntity.created(URI("/group7api/$user_id/projects/${project_id}/issues/$issue_id")).build()
    }

    @DeleteMapping("/{issue_id}/labels") /// Remove label
    fun deleteLabel(
        @PathVariable user_id: Int,
        @PathVariable project_id: Int,
        @PathVariable issue_id: Int,
        @RequestBody labelInputModel: LabelInputModel)
    : ResponseEntity<URI>{

        issueService.removeLabelFromIssue(IssueHasLabel(project_id, issue_id, labelInputModel.label))

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{issue_id}/state")
    fun changeState(
            @PathVariable issue_id: Int,
            @PathVariable project_id: Int,
            @RequestBody stateInputModel: StateInputModel
    ): ResponseEntity<URI>{

        issueService.changeState(issue_id, stateInputModel.new_state, project_id)

        return ResponseEntity.noContent().build()
    }

    @GetMapping()
    fun getIssueList(
            request: HttpServletRequest,
            @PathVariable project_id: Int,
            @PathVariable user_id: Int,
    ): SirenEntity<IssueListOutputModel>{

        val pageIndex: Int
        val pageSize: Int
        val issues: List<Issue>
        val indexes: List<Int>
        val page: List<Issue>

        try {
            val queryStringOptions = request.queryString.split("&")
            pageIndex = Integer.parseInt(queryStringOptions[0].split("=")[1])
            pageSize = Integer.parseInt(queryStringOptions[1].split("=")[1])
            println("$pageIndex, $pageSize")
            issues = issueService.getIssueList(project_id, pageIndex)
            indexes = utils.getIndexes(issues, pageIndex, pageSize)
            page = issues.subList(indexes[0], indexes[1])
        } catch (e: Exception) {
            throw InvalidQueryStringException()
        }

        val totalPages = ceil(issues.size.toDouble() / pageSize.toDouble())

        val previousPageIndex: Int = if (pageIndex == 1) {
            1
        } else pageIndex - 1

       val subs = page.map {
           val user = userService.getUserAccountInfo(it.user_id)
           EmbeddedEntity(
                   listOf("issue"),
                   listOf("item"),
                   it,
                   entities = listOf(
                           EmbeddedEntity(
                                   listOf("user"),
                                   listOf("owner"),
                                   UserOutputModel(user.user_id,user.username),
                                   null,
                                   null,
                                   listOf(selfLink("/group7api/users/${user.user_id}/"))

                           )
                   ),
                   null,
                   listOf(selfLink("/group7api/${user_id}/projects/${project_id}/issues/${it.issue_id}"))
           )
           }

            return IssueListOutputModel(issues.size, totalPages.toInt(), pageIndex, pageSize, project_id).toSirenObject(
                    entities = subs,
                    actions = listOf(createIssueAction(user_id,project_id)),
                    links = listOf(
                            selfLink("/group7api/${user_id}/projects/${project_id}/issues?PageIndex=$pageIndex&PageSize=$pageSize"),
                            SirenLink(
                                    listOf("previous"),
                                    "/group7api/${user_id}/projects/${project_id}/issues?pageIndex=${previousPageIndex}&pageSize=$pageSize"
                            ),
                            SirenLink(
                                    listOf("next"),
                                    "/group7api/${user_id}/projects/${project_id}/issues?pageIndex=${pageIndex + 1}&pageSize=$pageSize"
                            ),
                        SirenLink(
                            listOf("owner"),
                            "/group7api/${user_id}/projects/${project_id}/"
                        )
                    )
            )
        }
}

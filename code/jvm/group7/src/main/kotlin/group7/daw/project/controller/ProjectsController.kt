package group7.daw.project.controller

import group7.daw.*
import group7.daw.common.Utils
import group7.daw.common.exceptions.InvalidQueryStringException
import group7.daw.project.models.*
import group7.daw.project.services.ProjectService
import group7.daw.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest
import kotlin.math.ceil

private val log = LoggerFactory.getLogger(ProjectsController::class.java)

@RestController
@RequestMapping("group7api/{user_id}/projects", produces = [SIREN_MEDIA_TYPE, MediaType.APPLICATION_JSON_VALUE])
class ProjectsController(
        private val projectService: ProjectService,
        private val userService: UserService,
        private val utils: Utils) {

    @GetMapping() // Receber a lista
    fun getUserProjectList(
        request: HttpServletRequest,
        @PathVariable user_id: Int
    ): SirenEntity<ProjectsCollectionOutputModel> {

        val pageIndex: Int
        val pageSize: Int
        val projects: List<Project>
        val indexes: List<Int>
        val page: List<Project>

        try {
            val queryStringOptions = request.queryString.split("&")
            pageIndex = Integer.parseInt(queryStringOptions[0].split("=")[1])
            pageSize = Integer.parseInt(queryStringOptions[1].split("=")[1])
            projects = projectService.getUserProjects(pageIndex, user_id)
            indexes = utils.getIndexes(projects, pageIndex, pageSize)
            page = projects.subList(indexes[0], indexes[1])
        } catch (e: Exception) {
            throw InvalidQueryStringException()
        }

        val previousPageIndex: Int = if (pageIndex == 1) {
            1
        } else pageIndex - 1



        val totalPages = ceil(projects.size.toDouble() / pageSize.toDouble())

        val subs = page.map {
            EmbeddedEntity(
                listOf("project"),
                listOf("item"),
                it,
               null,
                null,
                listOf(selfLink("/group7api/$user_id/projects/${it.project_id}"))
            )
        }

        return ProjectsCollectionOutputModel(projects.size, totalPages.toInt(), pageIndex, pageSize, user_id).toSirenObject(
            entities = subs,
            actions = listOf(createProjectAction(user_id)),
            links = listOf(
                selfLink("/group7api/$user_id/projects?PageIndex=$pageIndex&PageSize=$pageSize"),
                SirenLink(
                    listOf("previous"),
                    "/group7api/$user_id/projects?pageIndex=${previousPageIndex}&pageSize=$pageSize"
                ),
                SirenLink(
                    listOf("next"),
                    "/group7api/$user_id/projects?pageIndex=${pageIndex + 1}&pageSize=$pageSize"
                )
            )
        )
    }

    @GetMapping("/{project_id}") //get details
    fun getProjectDetails(
        @PathVariable project_id: Int,
        @PathVariable user_id: Int
    ): SirenEntity<ProjectOutputModel> {

        val project = projectService.getProjectDetails(project_id)

        val user = userService.getUserAccountInfo(user_id)

        return ProjectOutputModel(project.name, project.description, project.project_id, project.labels, project.states, project.transitions).toSirenObject(
            entities = listOf(
                EmbeddedEntity(
                    clazz = listOf("user"),
                    rel = listOf("owner"),
                    properties = user, //props
                    links = listOf(selfLink("/group7api/user/$user_id"))
                )
            ),
            actions = listOf(editProjectAction(project.user_id, project_id), deleteProjectAction(project.user_id, project_id)),
            links = listOf(
                selfLink("/group7api/${project.user_id}/projects/${project_id}"),
                SirenLink(
                    rel = listOf("project","collection"),
                    href = "/group7api/${project.user_id}/projects?PageIndex={index}&PageSize={pageSize}"
                ),
                    SirenLink(
                            rel = listOf("issues","collection"),
                            href = "/group7api/${project.user_id}/projects/$project_id/issues?PageIndex={index}&PageSize={pageSize}"
                    )
            )
        )

    }

    @PostMapping() //criar
    fun createProjectHandler(
        @RequestBody projectInput: ProjectInputModel,
        @PathVariable user_id: Int
    ): ResponseEntity<URI> {

        log.info("Projects Controller - Received a request to create a project.")

        val user = userService.getUserAccountInfo(user_id)

        val project_id = projectService.createProject(user, projectInput)

        return ResponseEntity.created(URI("/group7api/$user_id/projects/${project_id}")).build()
    }

    @PutMapping("/{project_id}") //edit details
    fun editProjectHandler(
        @PathVariable user_id: Int,
        @PathVariable project_id: Int,
        @RequestBody projectInput: ProjectInputModel
    ): ResponseEntity<URI> {

        val user = userService.getUserAccountInfo(user_id)

        val project = projectService.editProject(user, projectInput, project_id)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{project_id}") //remove project
    fun removeProjectHandler(
            @PathVariable project_id: Int,
            @PathVariable user_id: Int)
    : ResponseEntity<URI> {

        projectService.deleteProject(project_id)

        return ResponseEntity.noContent().build()
    }
}
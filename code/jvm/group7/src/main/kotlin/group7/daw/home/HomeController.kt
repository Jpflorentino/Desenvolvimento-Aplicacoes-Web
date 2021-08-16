package group7.daw.home

import group7.daw.*
import group7.daw.common.Utils
import group7.daw.common.exceptions.InvalidQueryStringException
import group7.daw.common.pipeline.USER_USERNAME_KEY
import group7.daw.project.models.Project
import group7.daw.project.models.ProjectsCollectionOutputModel
import group7.daw.project.models.toSirenObject
import group7.daw.project.services.ProjectService
import group7.daw.user.models.UserOutputModel
import group7.daw.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest
import kotlin.math.ceil
import kotlin.math.roundToInt

private val logger = LoggerFactory.getLogger(Group7Application::class.java)

@RestController
@RequestMapping("group7api", produces = [SIREN_MEDIA_TYPE, MediaType.APPLICATION_JSON_VALUE])
class HomeController(val projectService: ProjectService, private val utils: Utils, val userService: UserService) {

    @GetMapping()
    fun getAPILinks(): APILinks{
        return APILinks()
    }

    @GetMapping("/home")
    fun getHomeResource(
        request: HttpServletRequest
    ): SirenEntity<HomeOutputModel> {

        if( (request.getAttribute(USER_USERNAME_KEY) ) != null ) {
            return HomeOutputModel().toSirenObject(
                links = listOf(
                    selfLink("/group7api"),
                    SirenLink(
                        listOf("projects"),
                        "/group7api/projects?PageIndex={index}&PageSize={pageSize}"
                    ),
                    SirenLink(
                        listOf("user"),
                        "/group7api/users/${utils.getUserIdByUsername(request.getAttribute(USER_USERNAME_KEY) as String)}"
                    )
                ),
                actions = listOf(createUserAction())
            )
        }

        // Se não está autenticado, não recebe link para o recurso user
        return HomeOutputModel().toSirenObject(
            links = listOf(
                selfLink("/group7api/home"),
                SirenLink(
                    listOf("projects"),
                    "/group7api/projects?PageIndex={index}&PageSize={pageSize}"
                )
            ),
            actions = listOf(createUserAction())
        )
    }

    /**
     * This handler is here instead of ProjectsController.kt bc that
     * mapping needs the userID in the request
     */
    @RequestMapping("/projects")
    @GetMapping //receber a lista de todos os projetos da aplicação
    fun getProjectList(request: HttpServletRequest): SirenEntity<ProjectsCollectionOutputModel> {

        val pageIndex: Int
        val pageSize: Int
        val projects: List<Project>
        val indexes: List<Int>
        val page: List<Project>

        try {
            val queryStringOptions = request.queryString.split("&")
            pageIndex = Integer.parseInt(queryStringOptions[0].split("=")[1])
            pageSize = Integer.parseInt(queryStringOptions[1].split("=")[1])
            projects = projectService.getProjectList(pageIndex)
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
            val user = userService.getUserAccountInfo(it.user_id)
            EmbeddedEntity(
                    listOf("project"),
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
                    listOf(selfLink("/group7api/${it.user_id}/projects/${it.project_id}"))
            )
        }

        return ProjectsCollectionOutputModel(projects.size, totalPages.toInt(), pageIndex, pageSize, null, ).toSirenObject(
            entities = subs,
            links = listOf(
                selfLink("/group7api/projects?pageIndex=$pageIndex&pageSize=$pageSize"),
                SirenLink(
                    listOf("previous"),
                    "/group7api/projects?pageIndex=${previousPageIndex}&pageSize=$pageSize"
                ),
                    SirenLink(
                            listOf("next"),
                            "/group7api/projects?pageIndex=${pageIndex + 1}&pageSize=$pageSize"
                    )
            )
        )
    }
}
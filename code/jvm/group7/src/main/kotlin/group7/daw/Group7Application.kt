package group7.daw

import com.fasterxml.jackson.annotation.JsonInclude
import group7.daw.comments.data.CommentRepository
import group7.daw.comments.service.CommentService
import group7.daw.common.Utils
import group7.daw.issue.data.IssueRepository
import group7.daw.issue.service.IssueService
import group7.daw.project.data.ProjectRepository
import group7.daw.project.services.ProjectService
import group7.daw.user.data.UserRepository
import group7.daw.user.services.UserService
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import javax.sql.DataSource

@SpringBootApplication
@ConfigurationPropertiesScan
class Group7Application(
    private val configProperties: ConfigProperties
) {
    @Bean
    fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()
        .serializationInclusion(JsonInclude.Include.NON_NULL)

    @Bean
    fun dataSource(): DataSource = PGSimpleDataSource().apply {
        setURL(configProperties.dbConnString)
    }

    @Bean
    fun jdbi(dataSource: DataSource): Jdbi = Jdbi.create(dataSource).apply {
        installPlugin(KotlinPlugin())
    }

    //Adicionados no contexto do trabalho
    @Bean
    fun getUserRepository(jdbi: Jdbi): UserRepository = UserRepository(jdbi)

    @Bean
    fun getIssueRepository(jdbi: Jdbi): IssueRepository = IssueRepository(jdbi)

    @Bean
    fun getProjectRepository(jdbi: Jdbi): ProjectRepository = ProjectRepository(jdbi)

    @Bean
    fun getCommentRepository(jdbi: Jdbi): CommentRepository = CommentRepository(jdbi)

    @Bean
    fun getUtils(projectRepository: ProjectRepository, userRepository: UserRepository, issueRepository: IssueRepository, commentRepository: CommentRepository): Utils =
            Utils(projectRepository, userRepository, issueRepository, commentRepository)

    @Bean
    fun getUserService(userRepository: UserRepository, utils: Utils): UserService = UserService(userRepository, utils)

    @Bean
    fun getProjectService(projectRepository: ProjectRepository, utils: Utils): ProjectService =
        ProjectService(projectRepository,utils)

    @Bean
    fun getIssueService(issueRepository: IssueRepository, userService: UserService, utils: Utils, projectRepository: ProjectRepository): IssueService =
            IssueService(issueRepository, projectRepository, utils)

    @Bean
    fun getCommentService(commentRepository: CommentRepository, utils: Utils): CommentService =
            CommentService(commentRepository, utils)
}

fun main(args: Array<String>) {
    runApplication<Group7Application>(*args)
}

package group7.daw

import group7.daw.project.models.Project
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DbTests {

    @Autowired
    lateinit var jdbi: Jdbi

    private val log = LoggerFactory.getLogger(Group7Application::class.java)

    @Test
    fun can_access_db() {
        val projects = jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.project")
                .mapTo(Project::class.java)
                .list()
        }
        log.info("Projects list: $projects")
        Assertions.assertEquals(true, projects.size > 0)
    }
}
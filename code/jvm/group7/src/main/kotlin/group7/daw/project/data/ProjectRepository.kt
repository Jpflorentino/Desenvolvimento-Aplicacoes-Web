package group7.daw.project.data

import group7.daw.Group7Application
import group7.daw.project.models.Project
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.slf4j.LoggerFactory
import java.util.*

data class State(val project_id: Int, val state_name: String)

data class Label(val project_id: Int, val label_name: String)

data class Transition(val project_id: Int, val state_name: String, val transits_to: String)

private val logger = LoggerFactory.getLogger(Group7Application::class.java)

class ProjectRepository(private val jdbi: Jdbi) {

    fun getProjectList(): List<Project> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.project")
                .mapTo(Project::class.java)
                .list()
        }
    }

    fun getUserProjects(user_id: Int): List<Project> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.project where user_id=:user_id")
                .bind("user_id", user_id)
                .mapTo(Project::class.java)
                .list()
        }
    }

    fun getProject(project_id: Int): Optional<Project> {

        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.project where project_id= (:project_id)")
                .bind("project_id", project_id)
                .mapTo(Project::class.java)
                .findFirst()
        }
    }

    fun createProject(user_id: Int, name: String, description: String): Int {

        logger.info("Projects Repository - Storing a project in postgres database server..")

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate(
                "insert into dbo.project(user_id, name, description) " +
                        "values (:user_id, :name, :description)"
            )
                .bind("user_id", user_id)
                .bind("name", name)
                .bind("description", description)
                .executeAndReturnGeneratedKeys("project_id")
                .mapTo(Int::class.java)
                .one()!!
        }

    }

    fun editProject(project: Project) {
        return jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("UPDATE dbo.project SET name=:name, description=:description WHERE project_id=:id")
                .bind("name", project.name)
                .bind("description", project.description)
                .bind("id", project.project_id)
                .execute()
        }
    }

    fun deleteProject(project_id: Int){
        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("delete from dbo.project where project_id=:project_id")
                    .bind("project_id", project_id)
                    .execute()
        }
    }


    //DONE
    //EXTRAS TO CREATE PROJECT
    fun insertAllowedLabels(allowed_labels_set: MutableList<String>, project_id: Int) {

        return jdbi.withHandleUnchecked { handle ->

            for (label in allowed_labels_set.distinct()) {
                handle.createUpdate("insert into dbo.label(project_id,label_name) values (:project_id, :label_name)")
                    .bind("project_id", project_id)
                    .bind("label_name", label)
                    .execute();
            }
        }
    }

    fun insertAllowedStates(allowed_states_set: MutableList<String>, project_id: Int) {

        return jdbi.withHandleUnchecked { handle ->

            for (state in allowed_states_set.distinct()) {
                handle.createUpdate("insert into dbo.state(project_id,state_name) values (:project_id, :state_name)")
                    .bind("project_id", project_id)
                    .bind("state_name", state)
                    .execute();
            }
        }
    }

    fun insertAllowedTransitions(allowed_transitions_set: MutableList<String>, project_id: Int) {

        return jdbi.withHandleUnchecked { handle ->

            for (transition in allowed_transitions_set.distinct()) {

                val first = transition.split(":")[0]
                val second = transition.split(":")[1]

                handle.createUpdate("insert into dbo.state_transits_to(project_id, state_name, transits_to) values (:project_id, :first, :second)")
                    .bind("project_id", project_id)
                    .bind("first", first)
                    .bind("second", second)
                    .execute();
            }
        }
    }

    //EXTRAS TO DELETE PROJECT
    fun deleteAllowedLabels(project_id: Int) {

        return jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("delete from dbo.label where project_id=:project_id")
                    .bind("project_id", project_id)
                    .execute()
        }
    }

    fun deleteAllowedStates(project_id: Int) {

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("delete from dbo.state where project_id=:project_id")
                    .bind("project_id", project_id)
                    .execute()
        }
    }

    fun deleteAllowedTransitions(project_id: Int) {

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("delete from dbo.state_transits_to where project_id=:project_id")
                    .bind("project_id", project_id)
                    .execute()
        }
    }

    //FUNCAO AUXILIAR PARA TABELA STATE
    fun getStateTable(project_id: Int): MutableList<String> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.state where project_id=:project_id")
                    .bind("project_id", project_id)
                    .mapTo(State::class.java)
                    .map {
                        it.state_name
                    }
                    .toMutableList()
        }
    }

    fun getLabelTable(project_id: Int): MutableList<String> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.label where project_id=:project_id")
                    .bind("project_id", project_id)
                    .mapTo(Label::class.java)
                    .map {
                        it.label_name
                    }
                    .toMutableList()
        }
    }

    fun getTransitionTable(project_id: Int): MutableList<Transition> {
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("SELECT * FROM dbo.state_transits_to where project_id=:project_id")
                    .bind("project_id", project_id)
                    .mapTo(Transition::class.java)
                    .toMutableList()
        }
    }
}

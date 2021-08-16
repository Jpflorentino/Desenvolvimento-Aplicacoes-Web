package group7.daw.project.services

import group7.daw.Group7Application
import group7.daw.common.Utils
import group7.daw.common.exceptions.ExceptionsProject
import group7.daw.common.exceptions.InvalidPageException
import group7.daw.project.data.ProjectRepository
import group7.daw.project.models.Project
import group7.daw.project.models.ProjectInputModel
import group7.daw.user.data.UserRepository
import group7.daw.user.models.User
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Group7Application::class.java)

class ProjectService(
        private val projectRepository: ProjectRepository,
        private val utils: Utils) {

    fun getProjectList(pageIndex: Int): List<Project> {

        if (pageIndex < 1)
            throw InvalidPageException()

        return projectRepository.getProjectList()
    }

    fun getUserProjects(pageIndex: Int, user_id: Int): List<Project> {

        if (pageIndex < 1)
            throw InvalidPageException()

        return projectRepository.getUserProjects(user_id)
    }

    fun getProjectDetails(project_id: Int): Project {

        val project = projectRepository.getProject(project_id).get()

        val labels = projectRepository.getLabelTable(project_id)

        project.labels.addAll(labels)

        val states = projectRepository.getStateTable(project_id)

        project.states.addAll(states)

        val transitions = projectRepository.getTransitionTable(project_id)

        project.transitions.addAll(transitions)

        return project
    }

    fun createProject(user: User, projectInput: ProjectInputModel): Int {

        logger.info("Projects Service - Creating a project..")

        if (projectInput.allowed_labels_set == null ||
            projectInput.allowed_states_set == null ||
            projectInput.allowed_transitions_set == null
        )
            throw ExceptionsProject.InvalidFieldsException()

        utils.validateProjectName(projectInput.name)

        var allowedLabels: MutableList<String> = mutableListOf()
        var allowedStates: MutableList<String> = mutableListOf()
        var allowedTransitions: MutableList<String> = mutableListOf()

        if(projectInput.allowed_labels_set != "")
            allowedLabels = utils.stringToList(projectInput.allowed_labels_set).toMutableList()

        if(projectInput.allowed_states_set != "")
            allowedStates = utils.stringToList(projectInput.allowed_states_set).toMutableList()

        if(projectInput.allowed_transitions_set != "")
            allowedTransitions = utils.stringToList(projectInput.allowed_transitions_set).toMutableList() //first:second

        //mandatory transition for every project
        allowedTransitions.add("closed:archived")
        allowedTransitions.add("closed:open")
        allowedTransitions.add("open:archived")
        allowedTransitions.add("open:closed")
        allowedTransitions.add("archived:closed")
        allowedTransitions.add("archived:open")

        // These states need to be present in the project no matter what
        allowedStates.add("closed")
        allowedStates.add("archived")
        allowedStates.add("open") //default state for an issue

        //in case the user doesn't provide any labels/states/transitions
        val filteredLabels = allowedLabels.filter{it != ""}.toMutableList()
        val fileteredTransitions = allowedTransitions.filter{it != ""}.toMutableList()
        val fileteredStates = allowedStates.filter{it != ""}.toMutableList()

        validateTransitions(fileteredStates, fileteredTransitions)

        val projectID = projectRepository.createProject(user.user_id, projectInput.name, projectInput.description)

        logger.info("project id= $projectID")

        if(filteredLabels.size != 0) projectRepository.insertAllowedLabels(filteredLabels, projectID) //if there aren't labels we don't add anything to the DB
        projectRepository.insertAllowedStates(fileteredStates, projectID)
        projectRepository.insertAllowedTransitions(fileteredTransitions, projectID)

        return projectID
    }

    fun editProject(user: User, projectInput: ProjectInputModel, project_id: Int): Project {

        val dbProject = getProjectDetails(project_id);

        if(dbProject.name != projectInput.name)
            utils.validateProjectName(projectInput.name)

        var allowedStates = projectRepository.getStateTable(project_id)

        val project = Project(
            user.user_id,
            project_id,
            projectInput.name,
            projectInput.description
        )

        /**
         * Unable to edit states bc of primary key constraints for the tables
         */
        if (projectInput.allowed_labels_set != "") {
            val allowedLabels = utils.stringToList(projectInput.allowed_labels_set!!).toMutableList()

            //Update of PK isnt' allowed, so we delete the previous entries and create new ones
            projectRepository.insertAllowedLabels(allowedLabels, project_id)
        }
        if (projectInput.allowed_states_set != "") {
            allowedStates = utils.stringToList(projectInput.allowed_states_set!!).toMutableList()
            projectRepository.insertAllowedStates(allowedStates, project_id)
        }
        if (projectInput.allowed_transitions_set != "") {
            val allowedTransitions = utils.stringToList(projectInput.allowed_transitions_set!!).toMutableList() //first:second
            validateTransitions(allowedStates, allowedTransitions)
            projectRepository.insertAllowedTransitions(allowedTransitions,project_id)
        }

        projectRepository.editProject(project)
        return project
    }

    fun deleteProject(project_id: Int){

        projectRepository.deleteProject(project_id)
    }


    //EXTRAS TO VALIDATE
    /**
     * helper function to see if all the transitions used in the project creation use
     * allowed states
     *
     * @return true = transition list is valid
     */
    private fun validateTransitions(states: MutableList<String>, transitions: MutableList<String>) {

        for (transition in transitions.distinct()) {

            val first = transition.split(":")[0]
            val second = transition.split(":")[1]

            if (first == "" || second == "")
                throw ExceptionsProject.InvalidFieldsException()

            if (!states.contains(first) || !states.contains(second))
                throw ExceptionsProject.InvalidFieldsException()

        }
    }
}
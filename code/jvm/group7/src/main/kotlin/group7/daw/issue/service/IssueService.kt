package group7.daw.issue.service

import group7.daw.common.Utils
import group7.daw.common.exceptions.InvalidNewLabelException
import group7.daw.common.exceptions.InvalidNewStateException
import group7.daw.common.exceptions.InvalidPageException
import group7.daw.common.exceptions.NoLabelException
import group7.daw.issue.controller.logger
import group7.daw.issue.data.IssueRepository
import group7.daw.issue.models.Issue
import group7.daw.issue.models.IssueInputModel
import group7.daw.issue.models.IssueHasLabel
import group7.daw.project.controller.ProjectsController
import group7.daw.project.data.ProjectRepository
import group7.daw.user.services.UserService
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(ProjectsController::class.java)

class IssueService(
    private val issueRepository: IssueRepository,
    private val projectRepository: ProjectRepository,
    private val utils: Utils) {

    fun createIssue(issueInputModel: IssueInputModel, project_id: Int, createdDate: Int, username: String): Int{

        log.info("Issues service - Opening an issue..")

        val issue = Issue(
                utils.getUserIdByUsername(username),
                project_id,
                null,
                issueInputModel.name,
                issueInputModel.description,
                createdDate,
                issueInputModel.closed_on,
                "open")

       return issueRepository.createIssue(issue)
    }

    fun editIssue(issueInputModel: IssueInputModel, issue_id: Int){

        issueRepository.editIssue(issueInputModel, issue_id)
    }

    fun getIssue(issue_id:Int): Issue{

        return issueRepository.getIssue(issue_id).get()
    }

    fun deleteIssue(issue_id:Int){

        return issueRepository.deleteIssue(issue_id)
    }

    fun addLabelToIssue(issueHasLabel: IssueHasLabel){

       //see if project own label that user wants to input
        val labels = projectRepository.getLabelTable(issueHasLabel.project_id)

        if(!labels.contains(issueHasLabel.label))
            throw InvalidNewLabelException()

        return issueRepository.addLabel(issueHasLabel)
    }

    fun removeLabelFromIssue(issueHasLabel: IssueHasLabel){

       val issueLabels = issueRepository.getIssueLabels(issueHasLabel.issue_id)

        if(!issueLabels.contains(issueHasLabel.label))
            throw NoLabelException()

        return issueRepository.removeLabel(issueHasLabel)
    }

    fun getIssueLabels(issue_id: Int): List<String>{
        return issueRepository.getIssueLabels(issue_id)
    }

    fun changeState(issue_id: Int, newState: String, project_id: Int){

        val transitions = projectRepository.getTransitionTable(project_id)

        val allowedTransitions = transitions.map { it.transits_to }

        if(allowedTransitions.contains(newState))
            issueRepository.changeState(issue_id, newState)
        else
            throw InvalidNewStateException()

    }

    fun getIssueList(project_id: Int, pageIndex: Int): List<Issue> {

        if (pageIndex < 1)
            throw InvalidPageException()

        return issueRepository.getIssueList(project_id)
    }

}
package group7.daw.issue.data

import group7.daw.issue.controller.logger
import group7.daw.issue.models.Issue
import group7.daw.issue.models.IssueHasLabel
import group7.daw.issue.models.IssueInputModel
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class IssueRepository(private val jdbi: Jdbi) {


    fun getIssue(issue_id:Int): Optional<Issue>{

        return jdbi.withHandleUnchecked { handle ->

            handle.createQuery("select * from dbo.issue where issue_id=:issue_id")
                    .bind("issue_id", issue_id)
                    .mapTo(Issue::class.java)
                    .findFirst()
        }

    }

    fun getIssueList(project_id:Int): List<Issue>{

        return jdbi.withHandleUnchecked { handle ->

            handle.createQuery("select * from dbo.issue where project_id=:project_id")
                    .bind("project_id", project_id)
                    .mapTo(Issue::class.java)
                    .list()
        }

    }

    fun getIssueLabels(issue_id: Int): List<String>{

        return jdbi.withHandleUnchecked { handle ->

            handle.createQuery("select label_name from dbo.issue_has_labels where issue_id=:issue_id")
                    .bind("issue_id", issue_id)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    fun createIssue(issue: Issue): Int{

        logger.info("Issues repository - Storing an issue on postgres database server..")

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate(
                    "insert into dbo.issue(user_id, project_id, name, description, opened_on, closed_on, state_name) " +
                            "values (:user_id, :project_id, :name, :description, :opened_on, :closed_on, :state_name)")
                    .bind("user_id", issue.user_id)
                    .bind("project_id", issue.project_id)
                    .bind("name", issue.name)
                    .bind("description", issue.description)
                    .bind("opened_on", issue.opened_on)
                    .bind("closed_on", issue.closed_on)
                    .bind("state_name", issue.state_name)
                    .executeAndReturnGeneratedKeys("issue_id")
                    .mapTo(Int::class.java)
                    .one()!!
        }
    }

    fun editIssue(issueInputModel: IssueInputModel, issue_id: Int){
        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate(
                    "update dbo.issue " +
                            "set name=:name, description=:description, closed_on=:closed_on " +
                            "where issue_id=:issue_id")
                    .bind("issue_id", issue_id)
                    .bind("name", issueInputModel.name)
                    .bind("description", issueInputModel.description)
                    .bind("closed_on", issueInputModel.closed_on)
                    .execute()
        }
    }

    fun deleteIssue(issue_id: Int){
        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("delete from dbo.issue where issue_id=:issue_id")
                    .bind("issue_id", issue_id)
                    .execute()
        }
    }

    fun addLabel(issueHasLabel: IssueHasLabel){
        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("insert into dbo.issue_has_labels(project_id, issue_id, label_name) " +
                    "values (:project_id, :issue_id, :label)")
                .bind("project_id", issueHasLabel.project_id)
                .bind("issue_id", issueHasLabel.issue_id)
                .bind("label", issueHasLabel.label)
                .execute()
        }
    }

    fun removeLabel(issueHasLabel: IssueHasLabel){
        return jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("delete from dbo.issue_has_labels where issue_id=:issue_id and label_name=:label")
                .bind("issue_id", issueHasLabel.issue_id)
                .bind("label", issueHasLabel.label)
                .execute()
        }
    }

    fun changeState(issue_id: Int, newState: String){

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate(
                    "update dbo.issue " +
                            "set state_name=:newState" +
                            " where issue_id=:issue_id")
                    .bind("issue_id", issue_id)
                    .bind("newState", newState)
                    .execute()
        }

    }
}
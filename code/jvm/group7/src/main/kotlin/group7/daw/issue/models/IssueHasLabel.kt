package group7.daw.issue.models

data class IssueHasLabel (
    val project_id: Int,
    val issue_id: Int,
    val label: String,
)
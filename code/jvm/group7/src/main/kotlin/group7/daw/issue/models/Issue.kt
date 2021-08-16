package group7.daw.issue.models

data class Issue(
        val user_id: Int,
        val project_id: Int,
        val issue_id: Int?,
        val name:String,
        val description: String,
        val opened_on: Int,
        val closed_on: Int?,
        val state_name: String
)
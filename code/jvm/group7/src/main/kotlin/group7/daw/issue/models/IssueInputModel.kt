package group7.daw.issue.models

data class IssueInputModel(
    var name:String,
    var description: String,
    var closed_on: Int?
)
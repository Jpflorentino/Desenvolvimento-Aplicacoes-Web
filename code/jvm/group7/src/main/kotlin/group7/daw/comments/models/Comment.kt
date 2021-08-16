package group7.daw.comments.models

data class Comment(
        val user_id: Int,
        val issue_id: Int,
        val comment_id:Int?,
        val comment_text: String,
        val creation_date:Int
)
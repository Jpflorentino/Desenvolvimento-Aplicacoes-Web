package group7.daw.comments.data

import group7.daw.comments.models.Comment
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class CommentRepository(private val jdbi: Jdbi) {

    fun getComment(comment_id:Int): Optional<Comment>{
        return jdbi.withHandleUnchecked {
            it.createQuery("select * from dbo.comment where comment_id=:comment_id")
                    .bind("comment_id",comment_id)
                    .mapTo(Comment::class.java)
                    .findFirst()
        }
    }

    fun getCommentList(issue_id: Int): List<Comment>{
        return jdbi.withHandleUnchecked {
            it.createQuery("select * from dbo.comment where issue_id=:issue_id")
                    .bind("issue_id",issue_id)
                    .mapTo(Comment::class.java)
                    .list()
        }
    }

    fun createComment(comment: Comment): Int{
        return jdbi.withHandleUnchecked {
            it.createUpdate("insert into dbo.comment(user_id, issue_id, comment_text, creation_date) values " +
                    "(:user_id, :issue_id, :comment_text, :creation_date)")
                    .bind("user_id",comment.user_id)
                    .bind("issue_id",comment.issue_id)
                    .bind("comment_text",comment.comment_text)
                    .bind("creation_date",comment.creation_date)
                    .executeAndReturnGeneratedKeys("comment_id")
                    .mapTo(Int::class.java)
                    .one()
        }
    }

    fun deleteComment(comment_id:Int){
        return jdbi.withHandleUnchecked {
            it.createUpdate("delete from dbo.comment where comment_id=:comment_id")
                    .bind("comment_id", comment_id)
                    .execute()
        }
    }

    fun editComment(comment_id:Int, comment_text: String){
        return jdbi.withHandleUnchecked {
            it.createUpdate(
                    "update dbo.comment " +
                            "set comment_text=:comment_text " +
                            "where comment_id=:comment_id")
                    .bind("comment_id", comment_id)
                    .bind("comment_text", comment_text)
                    .execute()
        }
    }


}
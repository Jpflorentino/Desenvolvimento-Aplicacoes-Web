package group7.daw.comments.service

import group7.daw.comments.data.CommentRepository
import group7.daw.comments.models.Comment
import group7.daw.comments.models.CommentInputModel
import group7.daw.common.Utils
import group7.daw.common.exceptions.InvalidPageException
import java.time.LocalDateTime
import java.time.ZoneOffset

class CommentService(private val commentRepository: CommentRepository, private val utils: Utils) {

    fun getComment(comment_id: Int): Comment{

        return commentRepository.getComment(comment_id).get()
    }

    fun getCommentList(issue_id:Int, pageIndex: Int): List<Comment>{

        if (pageIndex < 1)
            throw InvalidPageException()

        return commentRepository.getCommentList(issue_id)
    }

    fun createComment(commentInputModel: CommentInputModel, issue_id:Int, username:String): Int{

        val comment = Comment(
                utils.getUserIdByUsername(username),
                issue_id,
                null,
                commentInputModel.comment_text,
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toInt()
        )

        return commentRepository.createComment(comment)
    }

    fun deleteComment(comment_id: Int){

        commentRepository.deleteComment(comment_id)
    }

    fun editComment(comment_id: Int, commentInputModel: CommentInputModel){

        commentRepository.editComment(comment_id, commentInputModel.comment_text)
    }
}
package group7.daw.user.data

import group7.daw.user.models.User
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import java.util.*

class UserRepository(private val jdbi: Jdbi) {

    fun checkIfUserExists(username: String): Optional<User>{
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select * from dbo.user_account where username = :username")
                    .bind("username", username)
                    .mapTo(User::class.java)
                    .findFirst()
        }
    }

    fun loginUser(user: User):Optional<User>{
        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select * from dbo.user_account where username = :username and password = :password")
                .bind("username", user.username)
                .bind("password", user.password)
                .mapTo(User::class.java)
                .findFirst()
        }
    }


    fun createUser(user: User): Int {
        return jdbi.withHandleUnchecked { handle ->
            handle.createUpdate("insert into dbo.user_account(username, password) values (:username, :password)")
                .bind("username", user.username)
                .bind("password", user.password)
                .executeAndReturnGeneratedKeys("user_id")
                .mapTo(Int::class.java)
                .one()!!
        }
    }

    fun getUserAccountInfo(user_id: Int): Optional<User> {

        return jdbi.withHandleUnchecked { handle ->

            handle.createQuery("select user_id, username from dbo.user_account where user_id = :user_id")
                .bind("user_id", user_id)
                    .mapTo(User::class.java)
                    .findFirst()
        }
    }

    fun editUser(username: String, newPassword: String){

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("update dbo.user_account set password=:newPassword where username = :username")
                    .bind("newPassword",newPassword)
                    .bind("username",username)
                    .execute()
        }
    }

    fun deleteUser(username: String){

        return jdbi.withHandleUnchecked { handle ->

            handle.createUpdate("delete from dbo.user_account where username = :username")
                    .bind("username",username)
                    .execute()
        }
    }
}
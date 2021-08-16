package group7.daw.user.services

import group7.daw.common.Utils
import group7.daw.common.exceptions.ExceptionsUser
import group7.daw.user.data.UserRepository
import group7.daw.user.models.User
import jdk.jshell.spi.ExecutionControl

class UserService(private val userRepository: UserRepository, private val utils: Utils) {

    fun createUser(user: User): Int {

        if (userRepository.checkIfUserExists(user.username).isPresent)
            throw ExceptionsUser.UsernameAlreadyInUseException();

        user.password = utils.hashString(user.password!!)
        return userRepository.createUser(user)
    }

    fun loginUser(user:User): Int{

        user.password = utils.hashString(user.password!!)

        val userReturned = userRepository.loginUser(user)

        if (!userReturned.isPresent)
            throw ExceptionsUser.InvalidCredentialsException();
        else
            return userReturned.get().user_id
    }

    fun getUserAccountInfo(user_id: Int): User {

        val user = userRepository.getUserAccountInfo(user_id);

        return user.get();
    }

    fun editUserAccount(user: User, newPassword: String): User {

        userRepository.editUser(user.username, utils.hashString(newPassword))

        return getUserAccountInfo(user.user_id)
    }

    fun deleteUserAccount(user: User) {

        userRepository.deleteUser(user.username)
    }
}
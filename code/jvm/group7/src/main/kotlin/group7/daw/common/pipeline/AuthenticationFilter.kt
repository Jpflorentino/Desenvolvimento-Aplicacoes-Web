package group7.daw.common.pipeline

import group7.daw.common.Utils
import group7.daw.user.models.User
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val log = LoggerFactory.getLogger(AuthenticationFilter::class.java)

const val USER_USERNAME_KEY = "user-username"

@Component
class AuthenticationFilter(private val jdbi: Jdbi, private val utils: Utils) : Filter {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        println("[${System.getenv("HOSTNAME") }] - Received a request")

        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val uri = httpRequest.requestURI

        val header = httpRequest.getHeader("Authorization")

        //Basic Authentication requires Authorization header
        if (header == null) {

            // home, create account and see all projects does not need authentication
            if (uri == "/group7api" || uri == "/group7api/users" || uri== "/group7api/users/login" || uri.contains("/group7api/projects") || uri == "/group7api/home") {
                chain?.doFilter(request, response)
            } else httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED

        } else {

            val base64Credentials = header.split(" ")[1] //Basic [coded string]

            val credentialsDecoded = String(Base64.getDecoder().decode(base64Credentials)) //username:password

            val username = credentialsDecoded.split(":")[0]
            val hashedPassword = utils.hashString(credentialsDecoded.split(":")[1])

            if (!validateUser(username, hashedPassword)) {
                log.error("Not provided or invalid credentials")
                httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
            } else {
                log.info("Auth Filter - User $username authenticated.")
                httpRequest.setAttribute(USER_USERNAME_KEY, username)
                chain?.doFilter(request, response)
            }

        }

    }

    fun verifyIsUserExists(username: String): Optional<User> {

        return jdbi.withHandleUnchecked { handle ->
            handle.createQuery("select * from dbo.user_account where username= :username")
                .bind("username", username)
                .mapTo(User::class.java)
                .findFirst()

        }
    }

    fun validateUser(username: String, password: String): Boolean {

        val user = verifyIsUserExists(username)

        return if (user.isEmpty)
            false
        else
            user.get().password == password
    }

}
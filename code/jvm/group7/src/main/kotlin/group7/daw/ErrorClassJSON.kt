package group7.daw

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Implementation note: This code is an adaptation of code that belongs to Prof. Paulo Pereira
 *
 * original link: https://github.com/isel-leic-daw/2021v-public/blob/main/LI61N/demos/hvac-spring-mvc/src/main/kotlin/isel/leic/daw/hvac/common/errors.kt
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorClassJSON(
        val type: String,
        val title: String,
        val detail: String,
        val status: Int
)
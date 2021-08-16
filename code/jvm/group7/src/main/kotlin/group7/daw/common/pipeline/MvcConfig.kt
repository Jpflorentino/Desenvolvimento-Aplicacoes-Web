package group7.daw.common.pipeline

import group7.daw.common.Utils
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class MvcConfig(private val utils: Utils) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AccessControlInterceptor(utils))
    }
}
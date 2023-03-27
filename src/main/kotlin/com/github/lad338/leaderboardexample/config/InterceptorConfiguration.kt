package com.github.lad338.leaderboardexample.config

import com.github.lad338.leaderboardexample.interceptor.AuthorizationInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfiguration(
    private val authorizationInterceptor: AuthorizationInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/scores")
    }
}
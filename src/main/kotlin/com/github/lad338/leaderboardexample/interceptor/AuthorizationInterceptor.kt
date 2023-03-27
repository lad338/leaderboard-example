package com.github.lad338.leaderboardexample.interceptor

import com.github.lad338.leaderboardexample.model.error.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthorizationInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val auth = request.getHeader("Authorization")
        if (auth != null && auth.isEmpty()) {
            if (auth.startsWith("Bearer ")) {
                return true
            }
        }
        throw UnauthorizedException()
    }
}
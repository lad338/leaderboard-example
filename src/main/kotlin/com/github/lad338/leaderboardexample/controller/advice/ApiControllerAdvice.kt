package com.github.lad338.leaderboardexample.controller.advice

import com.github.lad338.leaderboardexample.model.error.LeaderboardNotFoundException
import com.github.lad338.leaderboardexample.model.error.UnauthorizedException
import com.github.lad338.leaderboardexample.model.response.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiControllerAdvice {
    @ExceptionHandler(value = [LeaderboardNotFoundException::class])
    fun handleLeaderboardNotFoundException(
        e: LeaderboardNotFoundException
    ): ResponseEntity<ErrorResponse<String>> {
        return ResponseEntity.status(404).body(ErrorResponse("leaderboard not found"))
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun handleUnauthorizedException(
        e: UnauthorizedException
    ): ResponseEntity<ErrorResponse<String>> {
        return ResponseEntity.status(401).body(ErrorResponse("unauthorized"))
    }
}
package com.github.lad338.leaderboardexample.controller

import com.github.lad338.leaderboardexample.model.request.PostScoreRequest
import com.github.lad338.leaderboardexample.service.LeaderboardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scores")
@Tag(name = "Score APIs", description = "Score APIs")
@SecurityRequirement(name = "auth")
class ScoreController(private val leaderboardService: LeaderboardService) {

    @PostMapping
    @Operation(
        description = "Add score to current month leaderboard and all time leaderboard if the score is greater than the record. Please use Authorize on the top right button to set the userId as the bearer token"
    )
    fun postScore(
        @Parameter(hidden = true) @RequestHeader("Authorization") authorization: String,
        @RequestBody request: PostScoreRequest
    ) {
        leaderboardService.saveToLeaderboard(getUserId(authorization), request.score)
    }


    private fun getUserId(authorization: String): String {
        return authorization.removePrefix("Bearer ")
    }
}





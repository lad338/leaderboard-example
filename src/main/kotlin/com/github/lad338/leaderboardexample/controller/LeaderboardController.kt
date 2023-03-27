package com.github.lad338.leaderboardexample.controller

import com.github.lad338.leaderboardexample.model.response.ErrorResponse
import com.github.lad338.leaderboardexample.model.response.GetLeaderBoardResponse
import com.github.lad338.leaderboardexample.service.LeaderboardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboards")
@Tag(name = "Leaderboard APIs", description = "Leaderboard APIs")
class LeaderboardController(private val leaderboardService: LeaderboardService) {

    @GetMapping("/{name}")
    @Operation(description = "Get leaderboard by name,\n{name} is either 'all_time' for all time leaderboard or 'YYYYMM' for monthly leaderboard")
    @ApiResponse(
        responseCode = "200",
        description = "Return leaderboard successfully",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = GetLeaderBoardResponse::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Leaderboard not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class)
        )]
    )
    fun getLeaderBoard(@PathVariable name: String): GetLeaderBoardResponse {
        return GetLeaderBoardResponse(leaderboardService.getLeaderboard(name))
    }
}





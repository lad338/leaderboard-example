package com.github.lad338.leaderboardexample.controller
import com.github.lad338.leaderboardexample.model.response.GetLeaderBoardResponse
import com.github.lad338.leaderboardexample.service.LeaderboardService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboards")
@Tag(name = "Leaderboard APIs", description = "Leaderboard APIs")
class LeaderboardController(private  val leaderboardService: LeaderboardService) {

    @GetMapping("/{name}")
    fun getLeaderBoard(@PathVariable  name: String): GetLeaderBoardResponse {
        return GetLeaderBoardResponse(leaderboardService.getLeaderboard(name))
    }
}





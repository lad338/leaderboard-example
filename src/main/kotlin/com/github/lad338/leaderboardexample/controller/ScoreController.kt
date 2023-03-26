package com.github.lad338.leaderboardexample.controller
import com.github.lad338.leaderboardexample.model.request.PostScoreRequest
import com.github.lad338.leaderboardexample.service.LeaderboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scores")
class ScoreController(private  val leaderboardService: LeaderboardService) {

    @PostMapping
    fun postScore(@RequestHeader("Authorization")  authorization: String, @RequestBody  request: PostScoreRequest) {
        leaderboardService.saveToLeaderboard(getUserId(authorization), request.score)
    }


    private fun getUserId(authorization: String): String {
        return authorization.removePrefix("Bearer ")
    }
}





package com.github.lad338.leaderboardexample.controller

import com.github.lad338.leaderboardexample.model.request.PostScoreRequest
import com.github.lad338.leaderboardexample.service.LeaderboardService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ScoreControllerUnitTest {

    private val leaderboardService: LeaderboardService = mockk()
    private val scoreController = ScoreController(leaderboardService)

    @Test
    fun givenAuthorizationAndRequest_whenPostScore_thenSaveToLeaderboard() {
        every { leaderboardService.saveToLeaderboard("DUMMY", 1.0) }.returns(Unit)
        scoreController.postScore("Bearer DUMMY", PostScoreRequest(1.0))
        verify { leaderboardService.saveToLeaderboard("DUMMY", 1.0) }
    }
}
package com.github.lad338.leaderboardexample.controller

import com.github.lad338.leaderboardexample.model.Leaderboard
import com.github.lad338.leaderboardexample.model.UserScore
import com.github.lad338.leaderboardexample.model.response.GetLeaderBoardResponse
import com.github.lad338.leaderboardexample.service.LeaderboardService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LeaderboardControllerTest {

    private val leaderboardService: LeaderboardService = mockk()
    private val leaderboardController = LeaderboardController(leaderboardService)

    @Test
    fun givenName_whenGetLeaderboard_thenGetFromServiceAndReturn() {
        val leaderboard = Leaderboard(
            listOf(
                UserScore("A", 10.0),
                UserScore("B", 5.0),
                UserScore("C", 1.0)
            )
        )

        every { leaderboardService.getLeaderboard("202303") }
            .returns(leaderboard)

        assertEquals(
            GetLeaderBoardResponse(leaderboard),
            leaderboardController.getLeaderBoard("202303")
        )

        verify { leaderboardService.getLeaderboard("202303") }

    }
}
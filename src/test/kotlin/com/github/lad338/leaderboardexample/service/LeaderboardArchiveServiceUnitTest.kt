package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.repository.LeaderboardRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class LeaderboardArchiveServiceUnitTest {

    private val leaderboardRepository: LeaderboardRepository = mockk(relaxed = true)
    private val leaderboardArchiveService = LeaderboardArchiveService(
        leaderboardRepository
    )

    @Test
    fun givenAny_whenGetLeaderboard_returnByCallingRepository() {
        leaderboardArchiveService.getLeaderboard("")
        verify { leaderboardRepository.getLeaderboardDocumentByName(any()) }
    }
}
package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.model.Leaderboard
import com.github.lad338.leaderboardexample.model.UserScore
import com.github.lad338.leaderboardexample.model.document.LeaderboardDocument
import com.github.lad338.leaderboardexample.repository.LeaderboardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class LeaderboardWorkerUnitTest {

    private val leaderboardRepository: LeaderboardRepository = mockk()
    private val leaderboardService: LeaderboardService = mockk()
    private val leaderboardWorker = LeaderboardWorker(
        leaderboardRepository,
        leaderboardService
    )
    private val previousMonth = leaderboardWorker.getPreviousMonthLeaderboardName()
    private val dummyString = "DUMMY"
    private val dummyScore = 123.45
    private val dummyUserScore = listOf(UserScore(dummyString, dummyScore))
    private val dummyDateTime = LocalDateTime.now()
    private val dummyDocument = LeaderboardDocument(
        dummyString,
        Leaderboard(dummyUserScore),
        true,
        dummyDateTime,
        dummyDateTime
    )

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun givenPreviousMonthNotInRepoAndNotEmpty_whenSavePreviousMonth_thenCallFromCacheAndSaveToRepo() {

        every {
            leaderboardRepository.getLeaderboardDocumentByName(
                previousMonth
            )
        }.returns(null)

        every {
            leaderboardService.getUserScoresFromCache(
                previousMonth
            )
        }.returns(dummyUserScore)

        every {
            leaderboardRepository.save(any())
        }.returns(dummyDocument)

        leaderboardWorker.savePreviousMonthLeaderboardToDatabase()

        verify { leaderboardRepository.getLeaderboardDocumentByName(previousMonth) }
        verify { leaderboardService.getUserScoresFromCache(previousMonth) }
        verify { leaderboardRepository.save(any()) }
    }

    @Test
    fun givenPreviousMonthNotInRepoButEmpty_whenSavePreviousMonth_thenDoNotSaveRepo() {

        every {
            leaderboardRepository.getLeaderboardDocumentByName(
                previousMonth
            )
        }.returns(null)

        every {
            leaderboardService.getUserScoresFromCache(
                previousMonth
            )
        }.returns(emptyList())

        leaderboardWorker.savePreviousMonthLeaderboardToDatabase()

        verify { leaderboardRepository.getLeaderboardDocumentByName(previousMonth) }
        verify { leaderboardService.getUserScoresFromCache(previousMonth) }
        verify(exactly = 0) { leaderboardRepository.save(any()) }
    }

    @Test
    fun givenPreviousMonthInRepo_whenSavePreviousMonth_thenDoNotFindCacheNorSaveRepo() {

        every {
            leaderboardRepository.getLeaderboardDocumentByName(
                previousMonth
            )
        }.returns(dummyDocument)

        leaderboardWorker.savePreviousMonthLeaderboardToDatabase()

        verify { leaderboardRepository.getLeaderboardDocumentByName(previousMonth) }
        verify(exactly = 0) { leaderboardService.getUserScoresFromCache(previousMonth) }
        verify(exactly = 0) { leaderboardRepository.save(any()) }
    }

    @Test
    fun givenCurrentCacheNotEmpty_whenBackupLeaderboards_thenSaveToRepo() {

        every {
            leaderboardService.getUserScoresFromCache(any())
        }.returns(dummyUserScore)

        every { leaderboardRepository.save(any()) }.returns(dummyDocument)

        leaderboardWorker.backupLeaderboards()

        verify(exactly = 2) { leaderboardService.getUserScoresFromCache(any()) }
        verify(exactly = 2) { leaderboardRepository.save(any()) }
    }

    @Test
    fun givenCurrentCacheEmpty_whenBackupLeaderboards_thenDoNotSaveToRepo() {

        every {
            leaderboardService.getUserScoresFromCache(any())
        }.returns(emptyList())

        leaderboardWorker.backupLeaderboards()

        verify(exactly = 2) { leaderboardService.getUserScoresFromCache(any()) }
        verify(exactly = 0) { leaderboardRepository.save(any()) }
    }
}
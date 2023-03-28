package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.constant.LeaderboardConstant
import com.github.lad338.leaderboardexample.model.Leaderboard
import com.github.lad338.leaderboardexample.model.document.LeaderboardDocument
import com.github.lad338.leaderboardexample.repository.LeaderboardRepository
import com.github.lad338.leaderboardexample.util.LeaderboardHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class LeaderboardWorker(
    private val leaderboardRepository: LeaderboardRepository,
    private val leaderboardService: LeaderboardService
) : LeaderboardHelper {

    //Scheduled for every minute for demonstration purpose
    @Scheduled(cron = "0 * * ? * *")
    fun savePreviousMonthLeaderboardToDatabase() {

        val previousMonth = getPreviousMonthLeaderboardName()
        // Save previous month leaderboard if previous month leaderboard is not in database and not empty in cache
        if (leaderboardRepository.getLeaderboardDocumentByName(previousMonth) == null) {
            val userScoresFromCache = leaderboardService.getUserScoresFromCache(previousMonth)
            if (userScoresFromCache.isNotEmpty()) {
                leaderboardRepository.save(
                    LeaderboardDocument(
                        previousMonth,
                        Leaderboard(userScoresFromCache),
                        true,
                    )
                )
            }
        }
    }

    //Scheduled for every minute for demonstration purpose
    @Scheduled(cron = "0 * * ? * *")
    fun backupLeaderboards() {
        backupLeaderboard(getCurrentMonthLeaderboardName())
        backupLeaderboard(LeaderboardConstant.ALL_TIME)
    }

    private fun backupLeaderboard(name: String) {
        val userScoresFromCache = leaderboardService.getUserScoresFromCache(name)
        // backup leaderboard to db from cache if not empty
        if (userScoresFromCache.isNotEmpty()) {
            leaderboardRepository.save(
                LeaderboardDocument(
                    name,
                    Leaderboard(userScoresFromCache),
                    false
                )
            )
        }
    }
}
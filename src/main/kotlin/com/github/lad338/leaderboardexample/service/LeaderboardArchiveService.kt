package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.constant.LeaderboardConstant
import com.github.lad338.leaderboardexample.model.document.LeaderboardDocument
import com.github.lad338.leaderboardexample.repository.LeaderboardRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class LeaderboardArchiveService(
    private val leaderboardRepository: LeaderboardRepository
) {

    @Cacheable(value = [LeaderboardConstant.ARCHIVE_PREFIX], key = "#name", unless = "#result == null")
    fun getLeaderboard(name: String): LeaderboardDocument? {
        //Get from database if cache miss, then save to cache
        return leaderboardRepository.getLeaderboardDocumentByName(name)
    }
}








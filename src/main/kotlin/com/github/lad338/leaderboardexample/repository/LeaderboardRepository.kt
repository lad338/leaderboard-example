package com.github.lad338.leaderboardexample.repository

import com.github.lad338.leaderboardexample.model.document.LeaderboardDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository("LeaderboardRepository")
interface LeaderboardRepository : MongoRepository<LeaderboardDocument, String> {
    fun getLeaderboardDocumentByName(name: String): LeaderboardDocument?
}
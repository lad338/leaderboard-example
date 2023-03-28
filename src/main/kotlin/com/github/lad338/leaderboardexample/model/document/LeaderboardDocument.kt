package com.github.lad338.leaderboardexample.model.document

import com.github.lad338.leaderboardexample.model.Leaderboard
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("leaderboard")
data class LeaderboardDocument(
    @Id val name: String,
    val leaderboard: Leaderboard,
    val isArchive: Boolean
)





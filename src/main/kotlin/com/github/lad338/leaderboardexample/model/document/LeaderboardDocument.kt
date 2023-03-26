package com.github.lad338.leaderboardexample.model.document

import com.github.lad338.leaderboardexample.model.Leaderboard
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("leaderboard")
data class LeaderboardDocument(
    @Id val name: String,
    val leaderboard: Leaderboard,
    val isArchive: Boolean,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now()
)





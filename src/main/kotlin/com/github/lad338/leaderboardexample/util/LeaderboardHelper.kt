package com.github.lad338.leaderboardexample.util

import com.github.lad338.leaderboardexample.constant.LeaderboardConstant
import java.time.LocalDateTime
import java.time.ZoneId

interface LeaderboardHelper {
    fun getCurrentMonthLeaderboardName(): String {
        val currentUTCDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))
        return getLeaderboardName(
            currentUTCDateTime.year,
            currentUTCDateTime.month.value
        )
    }

    fun getPreviousMonthLeaderboardName(): String {
        val currentUTCDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))
        return getLeaderboardName(
            currentUTCDateTime.minusMonths(1).year,
            currentUTCDateTime.minusMonths(1).month.value
        )
    }

    fun getLeaderboardName(year: Int, month: Int): String {
        val monthString = if (month >= 10) month.toString() else "0$month"
        return "$year$monthString"
    }

    fun isOngoingLeaderboard(name: String): Boolean {
        return name == getCurrentMonthLeaderboardName() ||
                name == LeaderboardConstant.ALL_TIME
    }
}
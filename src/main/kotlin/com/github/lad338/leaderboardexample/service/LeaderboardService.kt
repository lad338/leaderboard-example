package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.constant.LeaderboardConstant.Companion.ALL_TIME
import com.github.lad338.leaderboardexample.model.Leaderboard
import com.github.lad338.leaderboardexample.model.UserScore
import com.github.lad338.leaderboardexample.model.error.LeaderboardNotFoundException
import com.github.lad338.leaderboardexample.util.LeaderboardHelper
import org.springframework.data.redis.connection.RedisZSetCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import org.springframework.stereotype.Service

@Service
class LeaderboardService(
    private val stringRedisSerializer: RedisSerializer<String>,
    private val redisTemplate: RedisTemplate<String, String>,
    private val leaderboardArchiveService: LeaderboardArchiveService
) : LeaderboardHelper {
    fun saveToLeaderboard(user: String, score: Double) {
        saveToLeaderboardIfHigherScore(user, score)
    }

    fun getLeaderboard(name: String): Leaderboard {

        return if (isOngoingLeaderboard(name)) {
            Leaderboard(getUserScoresFromCache(name))
        } else {
            leaderboardArchiveService.getLeaderboard(name)?.leaderboard ?: throw LeaderboardNotFoundException()
        }
    }

    fun getUserScoresFromCache(name: String): List<UserScore> {
        return redisTemplate
            .opsForZSet()
            .reverseRangeWithScores(
                getLeaderboardKey(name),
                0,
                9
            )?.map { UserScore(it.value!!, it.score!!) }
            .orEmpty()
    }

    private fun saveToLeaderboardIfHigherScore(user: String, score: Double) {

        val allTimeKey = mustSerializeString(getLeaderboardKey(ALL_TIME))
        val monthKey = mustSerializeString(getLeaderboardKey(getCurrentMonthLeaderboardName()))
        val value = mustSerializeString(user)

        redisTemplate.execute { connection ->
            connection.zSetCommands().zAdd(
                allTimeKey, score, value, RedisZSetCommands.ZAddArgs.empty().gt()
            )

            connection.zSetCommands().zAdd(
                monthKey, score, value, RedisZSetCommands.ZAddArgs.empty().gt()
            )
        }
    }

    private fun mustSerializeString(s: String): ByteArray {
        return stringRedisSerializer.serialize(s) ?: throw SerializationException("Cannot serialize: $s")
    }
}


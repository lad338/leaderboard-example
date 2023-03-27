package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.constant.LeaderboardConstant
import com.github.lad338.leaderboardexample.model.Leaderboard
import com.github.lad338.leaderboardexample.model.UserScore
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
        saveToLeaderboardIfHigherScore(user, score, getLeaderboardKey(getCurrentMonthLeaderboardName()))
        saveToLeaderboardIfHigherScore(user, score, getLeaderboardKey(LeaderboardConstant.ALL_TIME))
    }

    fun getLeaderboard(name: String): Leaderboard {

        return if (isOngoingLeaderboard(name)) {
            //TODO handle 404
            Leaderboard(getUserScoresFromCache(name))
        } else {
            //TODO handle 404
            leaderboardArchiveService.getLeaderboard(name)?.leaderboard ?: throw NotImplementedError()
        }
    }

    fun getUserScoresFromCache(name: String): List<UserScore> {
        //TODO handle !!
        return redisTemplate
            .opsForZSet()
            .reverseRangeWithScores(
                getLeaderboardKey(name),
                0,
                9
            )?.map { UserScore(it.value!!, it.score!!) }
            .orEmpty()
    }

    private fun saveToLeaderboardIfHigherScore(user: String, score: Double, leaderboardName: String) {
        val key = mustSerializeString(leaderboardName)
        val value = mustSerializeString(user)

        redisTemplate.execute { connection ->
            connection.zSetCommands().zAdd(
                key, score, value, RedisZSetCommands.ZAddArgs.empty().gt()
            )
        }
    }

    private fun mustSerializeString(s: String): ByteArray {
        return stringRedisSerializer.serialize(s) ?: throw SerializationException("Cannot serialize: $s")
    }
}


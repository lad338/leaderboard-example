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
        //serialize key and value to ByteArray
        val allTimeKey = mustSerializeString(getLeaderboardKey(ALL_TIME))
        val monthKey = mustSerializeString(getLeaderboardKey(getCurrentMonthLeaderboardName()))
        val value = mustSerializeString(user)

        // Run ZADD command for all time leaderboard and current month leaderboard
        redisTemplate.execute { connection ->
            connection.zSetCommands().zAdd(
                allTimeKey, score, value, RedisZSetCommands.ZAddArgs.empty().gt()
            )

            connection.zSetCommands().zAdd(
                monthKey, score, value, RedisZSetCommands.ZAddArgs.empty().gt()
            )
        }
    }

    fun getLeaderboard(name: String): Leaderboard {
        //if ongoing leaderboard (all time or current month) then get user scores from redis
        return if (isOngoingLeaderboard(name)) {
            Leaderboard(getUserScoresFromCache(name))
        } else {
            //get from cache
            leaderboardArchiveService.getLeaderboard(name)?.leaderboard ?: throw LeaderboardNotFoundException()
        }
    }

    fun getUserScoresFromCache(name: String): List<UserScore> {
        // Redis ZRANGE REV command
        // FROM 0 9 (inclusive)
        return redisTemplate
            .opsForZSet()
            .reverseRangeWithScores(
                getLeaderboardKey(name),
                0,
                9
            )?.map { UserScore(it.value!!, it.score!!) }
            .orEmpty()
    }

    private fun mustSerializeString(s: String): ByteArray {
        return stringRedisSerializer.serialize(s) ?: throw SerializationException("Cannot serialize: $s")
    }
}


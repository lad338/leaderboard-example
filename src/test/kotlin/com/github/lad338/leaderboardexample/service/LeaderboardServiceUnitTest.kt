package com.github.lad338.leaderboardexample.service

import com.github.lad338.leaderboardexample.constant.LeaderboardConstant.Companion.ALL_TIME
import com.github.lad338.leaderboardexample.model.Leaderboard
import com.github.lad338.leaderboardexample.model.UserScore
import com.github.lad338.leaderboardexample.model.document.LeaderboardDocument
import com.github.lad338.leaderboardexample.model.error.LeaderboardNotFoundException
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisZSetCommands
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.data.redis.serializer.RedisSerializer

@ExtendWith(MockKExtension::class)
class LeaderboardServiceUnitTest {

    private val stringRedisSerializer: RedisSerializer<String> = RedisSerializer.string()
    private val redisTemplate: RedisTemplate<String, String> = mockk()
    private val leaderboardArchiveService: LeaderboardArchiveService = mockk()

    private val leaderboardService: LeaderboardService =
        LeaderboardService(
            stringRedisSerializer,
            redisTemplate,
            leaderboardArchiveService
        )

    private val currentMonthLeaderboardName = leaderboardService.getCurrentMonthLeaderboardName()
    private val previousMonthLeaderboardName = leaderboardService.getPreviousMonthLeaderboardName()
    private val dummyString = "DUMMY"


    @BeforeEach
    fun setUp() {
        val zSetOps = mockk<ZSetOperations<String, String>>()

        every { redisTemplate.opsForZSet() }.returns(zSetOps)
        every {
            zSetOps.reverseRangeWithScores(
                leaderboardService.getLeaderboardKey(dummyString), 0, 9
            )
        }.returns(
            emptySet()
        )
        every {
            zSetOps.reverseRangeWithScores(
                leaderboardService.getLeaderboardKey(currentMonthLeaderboardName), 0, 9
            )
        }.returns(
            setOf(
                ZSetOperations.TypedTuple.of("A", 300.0),
                ZSetOperations.TypedTuple.of("B", 50.0),
                ZSetOperations.TypedTuple.of("C", 10.0)
            )
        )
        every {
            zSetOps.reverseRangeWithScores(
                leaderboardService.getLeaderboardKey(ALL_TIME), 0, 9
            )
        }.returns(
            setOf(
                ZSetOperations.TypedTuple.of("A", 300.0),
                ZSetOperations.TypedTuple.of("B", 100.0),
                ZSetOperations.TypedTuple.of("C", 10.0)
            )
        )
        every {
            leaderboardArchiveService.getLeaderboard(
                previousMonthLeaderboardName
            )
        }.returns(
            LeaderboardDocument(
                leaderboardService.getLeaderboardKey(previousMonthLeaderboardName),
                Leaderboard(
                    listOf(
                        UserScore("A", 200.0),
                        UserScore("B", 100.0),
                        UserScore("C", 1.0),
                    )
                ),
                true
            )
        )


        val s = slot<RedisCallback<Boolean>>()
        val connection = mockk<RedisConnection>()
        val zSetCommands = mockk<RedisZSetCommands>()

        every { connection.zSetCommands() }.returns(zSetCommands)
        every { zSetCommands.zAdd(any(), any(), any(), any()) }.returns(true)


        every {
            redisTemplate.execute(capture(s))
        }.answers {
            s.captured.doInRedis(connection)
        }
    }

    @Test
    fun givenCurrentMonth_whenGetLeaderboard_thenGetUserScoresFromCacheAndReturnSuccessfully() {
        assertEquals(
            Leaderboard(
                listOf(
                    UserScore("A", 300.0),
                    UserScore("B", 50.0),
                    UserScore("C", 10.0),
                )
            ),
            leaderboardService.getLeaderboard(currentMonthLeaderboardName)
        )
    }

    @Test
    fun givenAllTime_whenGetLeaderboard_thenGetUserScoresFromCacheAndReturnSuccessfully() {
        assertEquals(
            Leaderboard(
                listOf(
                    UserScore("A", 300.0),
                    UserScore("B", 100.0),
                    UserScore("C", 10.0),
                )
            ),
            leaderboardService.getLeaderboard(ALL_TIME)
        )
    }

    @Test
    fun givenPreviousMonth_whenGetLeaderboard_thenGetUserScoresFromArchiveServiceAndReturnSuccessfully() {
        assertEquals(
            Leaderboard(
                listOf(
                    UserScore("A", 200.0),
                    UserScore("B", 100.0),
                    UserScore("C", 1.0),
                )
            ),
            leaderboardService.getLeaderboard(previousMonthLeaderboardName)
        )
    }

    //TODO test for get null from archive service

    @Test
    fun givenNoPreviousLeaderboard_whenGetLeaderboard_thenThrowNotFound() {
        every {
            leaderboardArchiveService.getLeaderboard(any())
        }.returns(null)

        try {
            leaderboardService.getLeaderboard("199701")
        } catch (e: Exception) {
            assertEquals(e::class, LeaderboardNotFoundException::class)
        }
    }

    @Test
    fun givenNotExistingName_whenGetUserScoresFromCache_returnEmptyList() {
        assertEquals(
            emptyList<UserScore>(),
            leaderboardService.getUserScoresFromCache(dummyString)
        )
    }

    @Test
    fun givenAllTime_whenGetUserScoresFromCache_returnSuccessfully() {
        assertEquals(
            listOf(
                UserScore("A", 300.0),
                UserScore("B", 100.0),
                UserScore("C", 10.0),
            ),
            leaderboardService.getUserScoresFromCache(ALL_TIME)
        )
    }

    @Test
    fun givenUserAndScore_whenSaveToLeaderboard_thenSaveCurrentMonthAndAllTime() {
        leaderboardService.saveToLeaderboard(dummyString, 123.45)
        verify { redisTemplate.execute(any<RedisCallback<Any>>()) }
    }
}
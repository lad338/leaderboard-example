package com.github.lad338.leaderboardexample.config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
class RedisConfig {
    @Value("\${redis.host}")
    lateinit var host: String

    @Value("\${redis.port}")
    lateinit var port: String

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val factory = LettuceConnectionFactory()
        factory.standaloneConfiguration.port = port.toInt()
        factory.standaloneConfiguration.hostName = host
        return factory
    }

    @Bean
    fun objectRedisSerializer(): RedisSerializer<Any> {
        return GenericJackson2JsonRedisSerializer()
    }

    @Bean
    fun stringRedisSerializer(): RedisSerializer<String> {
        return RedisSerializer.string()
    }
}





package com.github.lad338.leaderboardexample

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.github.lad338.leaderboardexample.repository"])
@EnableScheduling
@OpenAPIDefinition(
    info = Info(
        title = "Leaderboard API service",
        version = "1.0"
    )
)
@SecurityScheme(name = "auth", scheme = "bearer", type = SecuritySchemeType.HTTP, `in` = SecuritySchemeIn.HEADER)
class LeaderboardExampleApplication

fun main(args: Array<String>) {
    runApplication<LeaderboardExampleApplication>(*args)
}





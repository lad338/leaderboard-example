package com.github.lad338.leaderboardexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.github.lad338.leaderboardexample.repository"])
@EnableScheduling
class LeaderboardExampleApplication

fun main(args: Array<String>) {
    runApplication<LeaderboardExampleApplication>(*args)
}





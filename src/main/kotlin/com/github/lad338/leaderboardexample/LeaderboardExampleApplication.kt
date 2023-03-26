package com.github.lad338.leaderboardexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.github.lad338.leaderboardexample.repository"])
class LeaderboardExampleApplication

fun main(args: Array<String>) {
    runApplication<LeaderboardExampleApplication>(*args)
}





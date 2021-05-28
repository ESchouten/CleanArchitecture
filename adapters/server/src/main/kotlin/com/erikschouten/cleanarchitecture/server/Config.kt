package com.erikschouten.cleanarchitecture.server

import com.erikschouten.cleanarchitecture.config.Config
import com.erikschouten.cleanarchitecture.config.DatabaseType
import com.erikschouten.cleanarchitecture.config.JDBC
import com.erikschouten.cleanarchitecture.config.JWTConfig
import io.ktor.application.*

fun Application.config() = environment.config.run {
    val databaseType = DatabaseType.valueOf(property("ktor.database.type").getString())
    Config(
        JWTConfig(
            domain = property("ktor.jwt.domain").getString(),
            audience = property("ktor.jwt.audience").getString(),
            realm = property("ktor.jwt.realm").getString()
        ),
        development = property("ktor.development").getString().toBoolean(),
        database = databaseType,
        jdbc = when (databaseType) {
            DatabaseType.JDBC -> JDBC(
                driver = property("ktor.database.jdbc.driver").getString(),
                url = property("ktor.database.jdbc.url").getString(),
                schema = property("ktor.database.jdbc.schema").getString(),
                username = property("ktor.database.jdbc.username").getString(),
                password = property("ktor.database.jdbc.password").getString(),
            )
            else -> null
        }
    )
}

package com.erikschouten.cleanarchitecture.server

import com.erikschouten.cleanarchitecture.config.Config
import com.erikschouten.cleanarchitecture.config.DatabaseType
import com.erikschouten.cleanarchitecture.config.JDBC
import com.erikschouten.cleanarchitecture.config.JWTConfig
import io.ktor.application.*

fun Application.config() = environment.config.run {
    val databaseType = DatabaseType.valueOf(property("database.type").getString())
    Config(
        jwt = JWTConfig(
            realm = property("jwt.realm").getString(),
            domain = property("jwt.domain").getString(),
            secret = property("jwt.secret").getString()
        ),
        development = property("ktor.development").getString().toBoolean(),
        database = databaseType,
        jdbc = when (databaseType) {
            DatabaseType.JDBC -> JDBC(
                driver = property("database.jdbc.driver").getString(),
                url = property("database.jdbc.url").getString(),
                schema = property("database.jdbc.schema").getString(),
                username = property("database.jdbc.username").getString(),
                password = property("database.jdbc.password").getString(),
            )
            else -> null
        }
    )
}

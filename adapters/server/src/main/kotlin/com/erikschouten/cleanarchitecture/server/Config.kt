package com.erikschouten.cleanarchitecture.server

import com.erikschouten.cleanarchitecture.config.Config
import com.erikschouten.cleanarchitecture.config.Database
import com.erikschouten.cleanarchitecture.config.JWTConfig
import io.ktor.application.*

fun Application.config() = environment.config.run {
    Config(
        JWTConfig(
            domain = property("ktor.jwt.domain").getString(),
            audience = property("ktor.jwt.audience").getString(),
            realm = property("ktor.jwt.realm").getString()
        ),
        development = property("ktor.development").getString().toBoolean(),
        database = Database.valueOf(property("ktor.database").getString()),
    )
}

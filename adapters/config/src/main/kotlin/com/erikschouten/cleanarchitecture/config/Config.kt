package com.erikschouten.cleanarchitecture.config

import com.erikschouten.cleanarchitecture.repositories.DatabaseFactory

data class Config(
    val jwt: JWTConfig,
    val development: Boolean,
    val database: Database,
) {
    init {
        if (database === Database.EXPOSED) {
            DatabaseFactory.init()
        }
    }
}

data class JWTConfig(
    val domain: String,
    val audience: String,
    val realm: String,
)

enum class Database {
    LOCAL, EXPOSED
}

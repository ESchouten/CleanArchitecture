package com.erikschouten.cleanarchitecture.config

import com.erikschouten.cleanarchitecture.repositories.DatabaseFactory

data class Config(
    val jwt: JWTConfig,
    val development: Boolean,
    val database: Database,
)

data class JWTConfig(
    val domain: String,
    val audience: String,
    val realm: String,
)

data class Database(
    val type: DatabaseType,
    val schema: String,
    val username: String,
    val password: String
) {
    init {
        if (type === DatabaseType.EXPOSED) {
            DatabaseFactory.init(schema, username, password)
        }
    }
}

enum class DatabaseType {
    LOCAL, EXPOSED
}

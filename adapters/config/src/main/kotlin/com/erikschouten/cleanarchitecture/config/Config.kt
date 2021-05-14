package com.erikschouten.cleanarchitecture.config

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

enum class Database {
    LOCAL, EXPOSED
}

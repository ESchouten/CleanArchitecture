package config

import repositories.DatabaseFactory

data class Config(
    val jwt: JWTConfig,
    val development: Boolean,
    val database: DatabaseType,
    val jdbc: JDBC?,
    val dropDB: Boolean
) {
    init {
        if (database === DatabaseType.JDBC) {
            DatabaseFactory.init(jdbc!!.driver, jdbc.url, jdbc.schema, jdbc.username, jdbc.password, dropDB)
        }
    }
}

data class JWTConfig(
    val domain: String,
    val secret: String,
    val realm: String,
)

data class JDBC(
    val driver: String,
    val url: String,
    val schema: String,
    val username: String,
    val password: String
)

enum class DatabaseType {
    LOCAL, JDBC
}

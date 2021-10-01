package server

import config.Config
import config.DatabaseType
import config.JDBC
import config.JWTConfig
import io.ktor.application.*

fun Application.config() = environment.config.run {
    val databaseType = DatabaseType.valueOf(property("database.type").getString())
    Config(
        jwt = JWTConfig(
            domain = property("jwt.domain").getString(),
            secret = property("jwt.secret").getString()
        ),
        development = property("ktor.development").getString().toBoolean(),
        database = databaseType,
        dropDB = property("database.drop").getString().toBoolean(),
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

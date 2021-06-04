package com.erikschouten.cleanarchitecture.repositories

import com.erikschouten.cleanarchitecture.repositories.user.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(
        driver: String,
        url: String,
        schema: String,
        username: String = "",
        password: String = "",
        development: Boolean = false
    ) {
        val config = HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url + schema
            this.username = username
            this.password = password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val ds = HikariDataSource(config)
        Database.connect(ds)

        if (development) {
            transaction {
                SchemaUtils.drop(UserTable)
                SchemaUtils.drop(Table("flyway_schema_history"))
            }
        }

        transaction {
            SchemaUtils.createMissingTablesAndColumns(UserTable)
        }

        Flyway.configure()
            .baselineOnMigrate(true)
            .locations("classpath:com/erikschouten/cleanarchitecture/repositories/db/migration")
            .dataSource(ds)
            .load()
            .migrate()
    }


    suspend fun <T> query(
        block: suspend () -> T
    ): T = newSuspendedTransaction { block() }
}

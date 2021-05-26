package com.erikschouten.cleanarchitecture.repositories

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(schema: String, username: String = "", password: String = "") {
        val config = HikariConfig().apply {
            driverClassName = "org.mariadb.jdbc.Driver"
            jdbcUrl = "jdbc:mariadb://localhost:3306/$schema"
            this.username = username
            this.password = password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        Database.connect(HikariDataSource(config))
        transaction {
            SchemaUtils.drop(UserTable)
            SchemaUtils.create(UserTable)
        }
    }

    suspend fun <T> query(
        block: suspend () -> T
    ): T = newSuspendedTransaction { block() }
}

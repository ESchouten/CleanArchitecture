package com.erikschouten.cleanarchitecture.repositories.db.migration

import com.erikschouten.cleanarchitecture.repositories.UserTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1__create_users: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(UserTable)
//            UserEntity.new {
//                email = Email("erik@erikschouten.com")
//                authorities = listOf(Authorities.USER)
//                //P@ssw0rd!
//                password = PasswordHash("\$2a\$12\$Mv.kPXkIeTgpwALj25ra8u.OXRcrQN7dnsTVE2jzHxjxL75EV6gZa")
//            }
        }
    }
}

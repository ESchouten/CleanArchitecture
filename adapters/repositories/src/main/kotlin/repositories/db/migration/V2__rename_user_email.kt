package repositories.db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class V2__rename_user_email : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            val conn = TransactionManager.current().connection
            val query = "ALTER TABLE `User` RENAME COLUMN IF EXISTS `name` TO `email`"
            val statement = conn.prepareStatement(query, false)
            statement.executeUpdate()
        }
    }
}


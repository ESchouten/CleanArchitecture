package repositories.utils

import domain.repository.Order
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> query(
    block: suspend () -> T
): T = newSuspendedTransaction { block() }

fun Order?.order() = when (this ?: Order.ASC) {
    Order.ASC -> SortOrder.ASC
    Order.DESC -> SortOrder.DESC
}







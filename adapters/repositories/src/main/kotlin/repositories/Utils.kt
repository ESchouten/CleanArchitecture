package repositories

import domain.repository.Order
import domain.repository.Pagination
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> query(
    block: suspend () -> T
): T = newSuspendedTransaction { block() }

fun Order?.order() = when (this ?: Order.ASC) {
    Order.ASC -> SortOrder.ASC
    Order.DESC -> SortOrder.DESC
}

fun <T> SizedIterable<T>.order(table: Table, pagination: Pagination) = order(table.columns, pagination)
fun <T> SizedIterable<T>.order(columns: List<Column<*>>, pagination: Pagination): SizedIterable<T> {
    return pagination.sort?.let { sort ->
        columns.find { it.name == sort.by }?.let { column ->
            orderBy(column to sort.order.order())
        }
    } ?: this
}

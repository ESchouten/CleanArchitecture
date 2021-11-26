package repositories

import domain.repository.Order
import domain.repository.Pagination
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> query(
    block: suspend () -> T
): T = newSuspendedTransaction { block() }

fun Order?.order() = when (this ?: Order.ASC) {
    Order.ASC -> SortOrder.ASC
    Order.DESC -> SortOrder.DESC
}

fun <T> SizedIterable<T>.order(table: Table, pagination: Pagination, default: Pair<Column<*>, Order>) =
    order(table.columns, pagination, default)

fun <T> SizedIterable<T>.order(
    table: Table,
    columns: List<Column<*>>,
    pagination: Pagination,
    default: Pair<Column<*>, Order>
) =
    order(table.columns + columns, pagination, default)

fun <T> SizedIterable<T>.order(
    columns: List<Column<*>>,
    pagination: Pagination,
    default: Pair<Column<*>, Order>
): SizedIterable<T> {
    val column = pagination.sort?.by?.let { by -> columns.find { it.name == by } } ?: default.first
    val order = (pagination.sort?.order ?: default.second).order()
    return orderBy(column to order)
}

fun SqlExpressionBuilder.search(
    tables: List<Table>,
    tablesColumns: Map<Table, List<Column<*>>?>,
    pagination: Pagination
) = search(tables.associateWith { null } + tablesColumns, pagination)

fun SqlExpressionBuilder.search(table: Table, pagination: Pagination) = search(table to null, pagination)
fun SqlExpressionBuilder.search(tables: List<Table>, pagination: Pagination) =
    search(tables.associateWith { null }, pagination)

fun SqlExpressionBuilder.search(table: Pair<Table, List<Column<*>>?>, pagination: Pagination) =
    search(mapOf(table), pagination)

fun SqlExpressionBuilder.search(tables: Map<Table, List<Column<*>>?>, pagination: Pagination) =
    pagination.search?.let { search ->
        var buffer: Op<Boolean>? = null
        tables.flatMap { doSearch(it.toPair(), search) }.forEach {
            buffer = buffer?.or(it) ?: it
        }
        buffer
    } ?: Op.TRUE

private fun SqlExpressionBuilder.doSearch(table: Pair<Table, List<Column<*>>?>, search: String) =
    doSearch(table.first, table.second ?: table.first.columns, search)

private fun SqlExpressionBuilder.doSearch(table: Table, columns: List<Column<*>>, search: String) = columns
    .filter { it.columnType is VarCharColumnType || it.columnType is TextColumnType }
    .filterIsInstance<Column<String>>()
    .map { (if (table is Alias<*>) table[it] else it) like "%${search}%" }



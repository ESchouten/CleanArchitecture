package repositories

import domain.repository.Order
import domain.repository.Pagination
import domain.repository.Sort
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> query(
    block: suspend () -> T
): T = newSuspendedTransaction { block() }

fun Order?.order() = when (this ?: Order.ASC) {
    Order.ASC -> SortOrder.ASC
    Order.DESC -> SortOrder.DESC
}

fun <ID : Comparable<ID>, E : Entity<ID>> SizedIterable<E>.limit(pagination: Pagination?) =
    pagination?.let { limit(it.itemsPerPage, it.offset()) } ?: this

fun <ID : Comparable<ID>, E : Entity<ID>> EntityClass<ID, E>.query(pagination: Pagination?) =
    (filters(pagination)?.let { find(it) } ?: all()).limit(pagination)

fun <ID : Comparable<ID>, E : Entity<ID>> EntityClass<ID, E>.filters(pagination: Pagination?): Op<Boolean>? {
    val filters = listOfNotNull(
        pagination?.search?.let {
            SqlExpressionBuilder.search(table, it)
        }
    ).toMutableList()
    return if (filters.isNotEmpty()) {
        var statement = filters.removeFirst()
        filters.forEach {
            statement = statement.and(it)
        }
        return statement
    } else null
}

fun <T> SizedIterable<T>.order(table: Table, sort: Sort?, default: Pair<Column<*>, Order>) =
    order(table.columns, sort, default)

fun <T> SizedIterable<T>.order(table: Table, columns: List<Column<*>>, sort: Sort?, default: Pair<Column<*>, Order>) =
    order(table.columns + columns, sort, default)

fun <T> SizedIterable<T>.order(
    columns: List<Column<*>>,
    sort: Sort?,
    default: Pair<Column<*>, Order>
): SizedIterable<T> {
    val column = sort?.by.let { by -> columns.find { it.name == by } } ?: default.first
    val order = (sort?.order ?: default.second).order()
    return orderBy(column to order)
}

fun SqlExpressionBuilder.search(tables: List<Table>, tablesColumns: Map<Table, List<Column<*>>?>, search: String) =
    search(tables.associateWith { null } + tablesColumns, search)

fun SqlExpressionBuilder.search(table: Table, search: String) = search(table to null, search)
fun SqlExpressionBuilder.search(tables: List<Table>, search: String) = search(tables.associateWith { null }, search)
fun SqlExpressionBuilder.search(table: Pair<Table, List<Column<*>>?>, search: String) =
    search(mapOf(table), search)

fun SqlExpressionBuilder.search(tables: Map<Table, List<Column<*>>?>, search: String): Op<Boolean>? {
    var buffer: Op<Boolean>? = null
    tables.flatMap { doSearch(it.toPair(), search) }.forEach {
        buffer = buffer?.or(it) ?: it
    }
    return buffer
}

private fun SqlExpressionBuilder.doSearch(table: Pair<Table, List<Column<*>>?>, search: String) =
    doSearch(table.first, table.second ?: table.first.columns, search)

private fun SqlExpressionBuilder.doSearch(table: Table, columns: List<Column<*>>, search: String) = columns
    .filter { it.columnType is VarCharColumnType || it.columnType is TextColumnType }
    .filterIsInstance<Column<String>>()
    .map { (if (table is Alias<*>) table[it] else it) like "%${search}%" }



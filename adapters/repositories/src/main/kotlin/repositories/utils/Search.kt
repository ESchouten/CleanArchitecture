package repositories.utils

import org.jetbrains.exposed.sql.*

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

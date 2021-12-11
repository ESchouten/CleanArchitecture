package repositories.utils

import org.jetbrains.exposed.sql.*

class SearchFields(
    val table: Table,
    val columns: List<Column<*>>
) {
    companion object {
        fun of(table: Table) = of(listOf(table))
        fun of(tables: List<Table>) = tables.map { SearchFields(it, it.columns) }
    }
}

fun SqlExpressionBuilder.search(fields: List<SearchFields>, search: String): Op<Boolean>? {
    var buffer: Op<Boolean>? = null
    fields.flatMap { doSearch(it, search) }.forEach {
        buffer = buffer?.or(it) ?: it
    }
    return buffer
}

private fun SqlExpressionBuilder.doSearch(fields: SearchFields, search: String) = fields.columns
    .filter { it.columnType is VarCharColumnType || it.columnType is TextColumnType }
    .filterIsInstance<Column<String>>()
    .map { (if (fields.table is Alias<*>) fields.table[it] else it) like "%${search}%" }

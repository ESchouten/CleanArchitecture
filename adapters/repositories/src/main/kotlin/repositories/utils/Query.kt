package repositories.utils

import domain.repository.Pagination
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and

fun <ID : Comparable<ID>, E : Entity<ID>> EntityClass<ID, E>.query(pagination: Pagination?) =
    (filters(pagination)?.let { find(it) } ?: all()).limit(pagination)

fun <ID : Comparable<ID>, E : Entity<ID>> EntityClass<ID, E>.filters(
    pagination: Pagination?,
    fields: List<SearchFields> = SearchFields.of(table)
): Op<Boolean>? {
    val filters = listOfNotNull(pagination?.search?.let { SqlExpressionBuilder.search(fields, it) }).toMutableList()
    return if (filters.isNotEmpty()) {
        var statement = filters.removeFirstOrNull()
        filters.forEach {
            statement = statement?.and(it)
        }
        return statement
    } else null
}

fun <ID : Comparable<ID>, E : Entity<ID>> SizedIterable<E>.limit(pagination: Pagination?) =
    pagination?.let { limit(it.itemsPerPage, it.offset()) } ?: this

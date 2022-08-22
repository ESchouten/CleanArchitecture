package domain.repository

import java.util.*

data class Pagination(
    val itemsPerPage: Int,
    val page: Int,
    val search: String? = null,
    val period: Period? = null,
    val sort: Sort? = null
) {
    fun offset() = page * itemsPerPage.toLong()
}

data class Sort(
    val by: String,
    val order: Order
)

enum class Order {
    ASC, DESC
}

data class Period(
    val from: Date,
    val to: Date
)

open class PaginationResult<T : Any>(
    val items: List<T>,
    val total: Long
)

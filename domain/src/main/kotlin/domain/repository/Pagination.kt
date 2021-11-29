package domain.repository

data class Pagination(
    val itemsPerPage: Int,
    val page: Int,
    val search: String? = null,
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

open class PaginationResult<T : Any>(
    val items: List<T>,
    val total: Long
) {
    constructor(pagination: PaginationResult<T>) : this(pagination.items, pagination.total)

    fun <U : Any> transform(transformer: (entity: T) -> U) = PaginationResult(items.map(transformer), total)
}

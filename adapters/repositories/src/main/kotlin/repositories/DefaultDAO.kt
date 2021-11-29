package repositories

import domain.repository.Order
import domain.repository.Pagination
import domain.repository.PaginationResult
import domain.repository.Repository
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass

abstract class DefaultDAO<Domain : Any, ID : Comparable<ID>, E : Entity<ID>>(
    private val dao: EntityClass<ID, E>
) : Repository<Domain, ID> {
    abstract fun E.toDomain(): Domain

    override suspend fun findById(id: ID) = query {
        dao.findById(id)?.toDomain()
    }

    override suspend fun findAll() = query {
        dao.all().map { it.toDomain() }
    }

    override suspend fun findAll(pagination: Pagination) = query {
        val query = dao.find { search(dao.table, pagination) }
        PaginationResult(
            query.copy()
                .order(dao.table, pagination, dao.table.id to Order.ASC)
                .limit(pagination.itemsPerPage, pagination.offset()).map { it.toDomain() },
            query.copy().count()
        )
    }

    override suspend fun delete(id: ID) = query {
        dao[id].delete()
    }

    override suspend fun count(pagination: Pagination?) = query {
        (pagination?.let {
            dao.find { search(dao.table, it) }
        } ?: dao.all()).count()
    }
}

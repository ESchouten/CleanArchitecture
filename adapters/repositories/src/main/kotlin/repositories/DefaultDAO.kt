package repositories

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
        PaginationResult(
            dao.all().limit(pagination.itemsPerPage, pagination.offset()).map { it.toDomain() },
            count()
        )
    }

    override suspend fun delete(id: ID) = query {
        dao[id].delete()
    }

    override suspend fun count() = query {
        dao.count()
    }
}

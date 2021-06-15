package com.erikschouten.cleanarchitecture.repositories

import com.erikschouten.cleanarchitecture.domain.repository.Repository
import com.erikschouten.cleanarchitecture.repositories.DatabaseFactory.query
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass

abstract class DefaultDAO<Domain, ID : Comparable<ID>, E : Entity<ID>>(
    private val dao: EntityClass<ID, E>
) : Repository<Domain, ID> {
    abstract fun E.toDomain(): Domain

    override suspend fun findById(id: ID) = query {
        dao.findById(id)?.toDomain()
    }

    override suspend fun findAll() = query {
        dao.all().map { it.toDomain() }
    }

    override suspend fun delete(id: ID) = query {
        dao[id].delete()
    }

    override suspend fun count() = query {
        dao.count()
    }
}

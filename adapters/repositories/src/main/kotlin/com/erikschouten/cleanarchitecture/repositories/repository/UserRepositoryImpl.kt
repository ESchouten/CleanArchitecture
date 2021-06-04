package com.erikschouten.cleanarchitecture.repositories.repository

import com.erikschouten.cleanarchitecture.domain.AlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.repositories.DatabaseFactory.query
import com.erikschouten.cleanarchitecture.repositories.table.UserEntity
import com.erikschouten.cleanarchitecture.repositories.table.UserTable
import java.util.*

class UserRepositoryImpl : UserRepository {
    override suspend fun findByEmail(email: Email) = query {
        UserEntity.find { UserTable.email eq email.value }.firstOrNull()?.toUser()
    }

    override suspend fun findById(id: UUID) = query {
        UserEntity.findById(id)?.toUser()
    }

    override suspend fun findAll() = query {
        UserEntity.all().map { it.toUser() }
    }

    override suspend fun create(entity: User) = query {
        UserEntity.new {
            email = entity.email
            authorities = entity.authorities
            password = entity.password
        }.toUser()
    }

    override suspend fun update(entity: User) = query {
        UserEntity[entity.id].apply {
            email = entity.email
            authorities = entity.authorities
            password = entity.password
        }.toUser()
    }
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<UUID, User>()

    override suspend fun findById(id: UUID) = users[id]
    override suspend fun findAll() = users.values.toList()
    override suspend fun create(entity: User): User {
        if (users.containsKey(entity.id)) throw AlreadyExistsException()
        return update(entity)
    }

    override suspend fun update(entity: User): User {
        users[entity.id] = entity
        return entity
    }

    override suspend fun findByEmail(email: Email) = users.values.find { it.email == email }
}

package com.erikschouten.cleanarchitecture.repositories

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val users: MutableList<User> = mutableListOf()

    override fun findById(id: Uuid) = users.find { it.id == id }
    override fun findAll() = users.toList()
    override fun save(entity: User): User {
        users.add(entity)
        return entity
    }

    override fun findByEmail(email: Email) = users.find { it.email == email }
}

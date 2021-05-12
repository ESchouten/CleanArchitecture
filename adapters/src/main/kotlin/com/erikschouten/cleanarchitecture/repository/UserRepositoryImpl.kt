package com.erikschouten.cleanarchitecture.repository

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entity.User

class UserRepositoryImpl : UserRepository {
    private val users: MutableList<User> = mutableListOf()

    override fun findById(id: Uuid) = users.find { it.id == id }
    override fun findAll() = users.toList()
    override fun save(entity: User): User {
        users.add(entity)
        return entity
    }

    override fun findByEmail(email: String) = users.find { it.email == email }
}

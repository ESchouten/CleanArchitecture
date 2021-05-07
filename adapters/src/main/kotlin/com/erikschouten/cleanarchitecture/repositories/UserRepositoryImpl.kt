package com.erikschouten.cleanarchitecture.repositories

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entities.User

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

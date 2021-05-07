package com.erikschouten.cleanarchitecture.repositories

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entities.User

interface UserRepository : Repository<User, Uuid> {
    fun findByEmail(email: String): User?
}

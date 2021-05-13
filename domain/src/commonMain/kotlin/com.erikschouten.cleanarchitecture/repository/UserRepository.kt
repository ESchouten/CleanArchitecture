package com.erikschouten.cleanarchitecture.repository

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entity.Email
import com.erikschouten.cleanarchitecture.entity.User

interface UserRepository : Repository<User, Uuid> {
    fun findByEmail(email: Email): User?
}

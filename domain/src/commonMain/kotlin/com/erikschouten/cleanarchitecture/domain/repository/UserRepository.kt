package com.erikschouten.cleanarchitecture.domain.repository

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.User

interface UserRepository : Repository<User, Uuid> {
    fun findByEmail(email: Email): User?
}

package com.erikschouten.cleanarchitecture.domain.repository

import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.User
import java.util.*

interface UserRepository : Repository<User, UUID> {
    suspend fun findByEmail(email: Email): User?
}

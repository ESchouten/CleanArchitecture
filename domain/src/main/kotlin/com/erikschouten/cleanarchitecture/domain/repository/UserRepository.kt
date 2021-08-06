package com.erikschouten.cleanarchitecture.domain.repository

import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.User

interface UserRepository : Repository<User, Int> {
    suspend fun findByEmail(email: Email): User?
    suspend fun findAllByEmails(emails: List<Email>): List<User>
}

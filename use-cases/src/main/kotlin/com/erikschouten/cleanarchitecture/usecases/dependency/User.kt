package com.erikschouten.cleanarchitecture.usecases.dependency

import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash
import java.util.*

interface Authenticator {
    fun generate(id: UUID): String
}

interface PasswordEncoder {
    fun encode(password: Password): PasswordHash
    fun matches(password: Password, hash: PasswordHash): Boolean
}

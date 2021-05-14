package com.erikschouten.cleanarchitecture.usecases.dependency

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash

interface Authenticator {
    fun generate(id: Uuid): String
}

interface PasswordEncoder {
    fun encode(password: Password): PasswordHash
    fun matches(password: Password, hash: PasswordHash): Boolean
}

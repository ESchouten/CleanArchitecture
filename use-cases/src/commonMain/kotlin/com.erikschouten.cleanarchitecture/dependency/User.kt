package com.erikschouten.cleanarchitecture.dependency

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entity.Password
import com.erikschouten.cleanarchitecture.entity.PasswordHash

interface Authenticator {
    fun generate(id: Uuid): String
}

interface PasswordEncoder {
    fun encode(password: Password): PasswordHash
    fun matches(password: Password, hash: PasswordHash): Boolean
}

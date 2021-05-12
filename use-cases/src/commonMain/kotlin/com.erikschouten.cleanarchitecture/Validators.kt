package com.erikschouten.cleanarchitecture

import com.benasher44.uuid.Uuid

interface Authenticator {
    fun generate(id: Uuid): String
}

interface PasswordEncoder {
    fun encode(password: String): String
    fun matches(password: String, hash: String): Boolean
}

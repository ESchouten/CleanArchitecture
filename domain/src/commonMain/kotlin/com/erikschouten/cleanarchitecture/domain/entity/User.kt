package com.erikschouten.cleanarchitecture.domain.entity

import com.erikschouten.cleanarchitecture.domain.EmailInvalidException
import com.erikschouten.cleanarchitecture.domain.PasswordInvalidException
import kotlin.jvm.JvmInline

data class User(
    val email: Email,
    val roles: List<Authorities>,
    val password: PasswordHash,
) : UUIDEntity()

enum class Authorities {
    USER
}

@JvmInline
value class Email(val value: String) {
    init {
        if (!value.contains('@')) throw EmailInvalidException()
    }
}

@JvmInline
value class Password(val value: String) {
    init {
        if (!value.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")))
            throw PasswordInvalidException()
    }
}

@JvmInline
value class PasswordHash(val value: String)

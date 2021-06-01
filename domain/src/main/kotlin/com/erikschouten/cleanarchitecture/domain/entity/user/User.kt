package com.erikschouten.cleanarchitecture.domain.entity.user

import com.erikschouten.cleanarchitecture.domain.EmailInvalidException
import com.erikschouten.cleanarchitecture.domain.PasswordInvalidException
import com.erikschouten.cleanarchitecture.domain.entity.UUIDEntity
import java.util.*

data class User(
    override val id: UUID = UUID.randomUUID(),
    val email: Email,
    val authorities: List<Authorities>,
    val password: PasswordHash,
) : UUIDEntity

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
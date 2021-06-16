package com.erikschouten.cleanarchitecture.domain.entity.user

import com.erikschouten.cleanarchitecture.domain.EmailInvalidException
import com.erikschouten.cleanarchitecture.domain.PasswordInvalidException
import com.erikschouten.cleanarchitecture.domain.entity.UUIDEntity
import com.erikschouten.cleanarchitecture.domain.entity.ValueClass
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

data class Email(override val value: String) : ValueClass<String> {
    init {
        if (!value.contains('@')) throw EmailInvalidException()
    }
}

data class Password(override val value: String) : ValueClass<String> {
    init {
        if (!value.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")))
            throw PasswordInvalidException()
    }
}

data class PasswordHash(override val value: String) : ValueClass<String>

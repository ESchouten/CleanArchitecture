package domain.entity.user

import domain.EmailInvalidException
import domain.PasswordInvalidException
import domain.entity.Entity
import domain.entity.ValueClass

data class User(
    override val id: Int = -1,
    val email: Email,
    val authorities: List<Authorities>,
    val password: PasswordHash,
) : Entity

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

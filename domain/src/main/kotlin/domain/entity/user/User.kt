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
    val locked: Boolean
) : Entity {
    override fun toString(): String {
        return "User(id=$id, email=$email, authorities=$authorities)"
    }
}

enum class Authorities {
    USER
}

data class Email(override val value: String) : ValueClass<String> {
    init {
        if (!value.contains('@')) throw EmailInvalidException()
    }
}

open class Password(override val value: String) : ValueClass<String> {
    override fun toString(): String {
        return "Password()"
    }
}

class NewPassword(value: String) : Password(value) {
    init {
        if (!value.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")))
            throw PasswordInvalidException()
    }

    override fun toString(): String {
        return "NewPassword()"
    }
}

data class PasswordHash(override val value: String) : ValueClass<String> {
    override fun toString(): String {
        return "PasswordHash()"
    }
}

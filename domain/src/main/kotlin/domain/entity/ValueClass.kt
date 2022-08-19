package domain.entity

import domain.EmailInvalidException
import domain.PasswordInvalidException

sealed interface ValueClass<T : Any> {
    val value: T
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

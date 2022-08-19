package domain.entity.user

import domain.entity.Email
import domain.entity.Entity
import domain.entity.PasswordHash

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

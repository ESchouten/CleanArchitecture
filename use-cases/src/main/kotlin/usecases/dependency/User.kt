package usecases.dependency

import domain.entity.user.Password
import domain.entity.user.PasswordHash

interface Authenticator {
    fun generate(id: Int): String
}

interface PasswordEncoder {
    fun encode(password: Password): PasswordHash
    fun matches(password: Password, hash: PasswordHash): Boolean
}

package usecases.dependency

import domain.entity.user.Password
import domain.entity.user.PasswordHash
import usecases.model.UserModel

interface Authenticator {
    fun generate(user: UserModel): String
}

interface PasswordEncoder {
    fun encode(password: Password): PasswordHash
    fun matches(password: Password, hash: PasswordHash): Boolean
}

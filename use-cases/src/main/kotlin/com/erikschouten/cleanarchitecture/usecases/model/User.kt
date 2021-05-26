package com.erikschouten.cleanarchitecture.usecases.model

import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import java.util.*

data class UserModel(
    val id: UUID,
    val email: String,
    val authorities: List<Authorities>,
) {
    constructor(user: User) : this(user.id, user.email.value, user.authorities)
}

data class CreateUserModel(
    val email: String,
    val authorities: List<Authorities>,
    val password: String,
) {
    fun toUser(encoder: PasswordEncoder) =
        User(email = Email(email), authorities = authorities, password = encoder.encode(Password(password)))
}

data class LoginUserModel(
    val email: String,
    val password: String,
)

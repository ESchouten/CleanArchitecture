package com.erikschouten.cleanarchitecture.usecases.model

import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import java.util.*

data class UserModel(
    val id: UUID,
    val email: Email,
    val authorities: List<Authorities>,
) {
    constructor(user: User) : this(user.id, user.email, user.authorities)
}

data class CreateUserModel(
    val email: Email,
    val authorities: List<Authorities>,
    val password: Password,
) {
    fun toUser(encoder: PasswordEncoder) =
        User(email = email, authorities = authorities, password = encoder.encode(password))
}

data class LoginUserModel(
    val email: Email,
    val password: Password,
)

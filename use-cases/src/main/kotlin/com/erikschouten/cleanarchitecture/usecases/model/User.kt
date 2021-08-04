package com.erikschouten.cleanarchitecture.usecases.model

import com.erikschouten.cleanarchitecture.domain.entity.user.*
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder

data class UserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
) {
    constructor(user: User) : this(user.id, user.email, user.authorities)
}

data class UpdateUserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
) {
    fun toUser(hash: PasswordHash) = User(email = email, authorities = authorities, password = hash)
}

data class ChangeOwnPasswordModel(
    val current: Password,
    val password: Password
)

data class ChangePasswordModel(
    val id: Int,
    val password: Password
)

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

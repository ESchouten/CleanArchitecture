package com.erikschouten.cleanarchitecture.model

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.PasswordEncoder
import com.erikschouten.cleanarchitecture.entity.Authorities
import com.erikschouten.cleanarchitecture.entity.User

data class UserModel(
    val id: Uuid,
    val email: String,
    val authorities: List<Authorities>,
) {
    constructor(user: User) : this(user.id, user.email, user.roles)
}

data class CreateUserModel(
    val email: String,
    val authorities: List<Authorities>,
    val password: String,
) {
    fun toUser(encoder: PasswordEncoder) = User(email, authorities, encoder.encode(password))
}

data class LoginUserModel(
    val email: String,
    val password: String,
)

package com.erikschouten.cleanarchitecture.usecases.model

import com.erikschouten.cleanarchitecture.domain.entity.user.*
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.common.ModelList
import java.util.*

class UserModelList(
    items: List<UserModel>
) : ModelList<UserModel>(items)

data class UserModel(
    val id: UUID,
    val email: Email,
    val authorities: List<Authorities>,
) {
    constructor(user: User) : this(user.id, user.email, user.authorities)
    fun toUser(hash: PasswordHash) = User(email = email, authorities = authorities, password = hash)
}

data class ChangeOwnPasswordModel(
    val current: Password,
    val password: Password
)

data class ChangePasswordModel(
    val id: UUID,
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

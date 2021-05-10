package com.erikschouten.cleanarchitecture.auth

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entities.Authorities
import io.ktor.auth.*
import com.erikschouten.cleanarchitecture.models.UserModel

class UserPrincipal private constructor(
    val id: Uuid,
    val email: String,
    val authorities: List<Authorities>,
) : Principal {
    constructor(user: UserModel) : this(user.id, user.email, user.authorities)
    fun toUserModel() = UserModel(id, email, authorities)
}

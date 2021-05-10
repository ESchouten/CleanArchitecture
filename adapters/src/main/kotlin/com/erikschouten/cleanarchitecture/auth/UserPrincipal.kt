package com.erikschouten.cleanarchitecture.auth

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entities.Authorities
import io.ktor.auth.*
import com.erikschouten.cleanarchitecture.models.UserModel

class UserPrincipal private constructor(
    private val id: Uuid,
    private val email: String,
    private val authorities: List<Authorities>,
) : Principal {
    constructor(user: UserModel) : this(user.id, user.email, user.authorities)
    fun toUserModel() = UserModel(id, email, authorities)
}

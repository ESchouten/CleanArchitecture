package com.erikschouten.cleanarchitecture.server.auth

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import io.ktor.auth.*

class UserPrincipal private constructor(
    private val id: Uuid,
    private val email: Email,
    private val authorities: List<Authorities>,
) : Principal {
    constructor(user: UserModel) : this(user.id, Email(user.email), user.authorities)
    fun toUserModel() = UserModel(id, email.value, authorities)
}

package com.erikschouten.cleanarchitecture.auth

import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.entity.Authorities
import com.erikschouten.cleanarchitecture.entity.Email
import io.ktor.auth.*
import com.erikschouten.cleanarchitecture.model.UserModel

class UserPrincipal private constructor(
    private val id: Uuid,
    private val email: Email,
    private val authorities: List<Authorities>,
) : Principal {
    constructor(user: UserModel) : this(user.id, Email(user.email), user.authorities)
    fun toUserModel() = UserModel(id, email.value, authorities)
}

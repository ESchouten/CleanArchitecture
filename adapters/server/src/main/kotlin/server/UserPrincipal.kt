package server

import domain.entity.user.Authorities
import domain.entity.user.Email
import usecases.model.UserModel
import io.ktor.auth.*

class UserPrincipal private constructor(
    private val id: Int,
    private val email: Email,
    private val authorities: List<Authorities>,
) : Principal {
    constructor(user: UserModel) : this(user.id, user.email, user.authorities)

    fun toUserModel() = UserModel(id, email, authorities)
}

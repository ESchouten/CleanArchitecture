package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.EmailAlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Mutation
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1
import java.util.*

@Mutation
class DeleteUser(
    private val repository: UserRepository,
) : UsecaseA1<UUID, Unit>(UUID::class, Unit::class) {

    override val executor: suspend (UserModel?, UUID) -> Unit = { authentication, a0 ->
        if (!authentication!!.authorities.contains(Authorities.USER)) throw AuthorizationException()
        repository.delete(a0)
    }
}

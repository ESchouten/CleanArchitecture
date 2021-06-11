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
) : UsecaseA1<UUID, Boolean>(UUID::class, Boolean::class) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, UUID) -> Boolean = { _, a0 ->
        repository.delete(a0)
        true
    }
}

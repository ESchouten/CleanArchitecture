package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.EmailAlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.UserNotFoundException
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Mutation
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1

@Mutation
class UpdateUser(
    private val repository: UserRepository,
    private val userExists: UserExists,
) : UsecaseA1<UserModel, UserModel>(UserModel::class, UserModel::class) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, UserModel) -> UserModel = { authentication, a0 ->
        val old = repository.findById(a0.id) ?: throw UserNotFoundException()
        if (old.email != a0.email && userExists(authentication, a0.email)) throw EmailAlreadyExistsException()
        UserModel(repository.update(a0.toUser(old.password)))
    }
}

package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.EmailAlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModelArray
import com.erikschouten.cleanarchitecture.usecases.usecase.Mutation
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA0
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1

@Query
class ListUsers(
    private val repository: UserRepository,
) : UsecaseA0<UserModelArray>(UserModelArray::class) {

    override val executor: suspend (UserModel?) -> UserModelArray = { authentication ->
        if (!authentication!!.authorities.contains(Authorities.USER)) throw AuthorizationException()
        UserModelArray(repository.findAll().map { UserModel(it) }.toTypedArray())
    }
}

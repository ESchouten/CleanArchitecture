package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.AuthorizationException
import com.erikschouten.cleanarchitecture.EmailAlreadyExistsException
import com.erikschouten.cleanarchitecture.EmailInvalidException
import com.erikschouten.cleanarchitecture.PasswordInvalidException
import com.erikschouten.cleanarchitecture.email
import com.erikschouten.cleanarchitecture.entities.Authorities
import com.erikschouten.cleanarchitecture.models.CreateUserModel
import com.erikschouten.cleanarchitecture.models.UserModel
import com.erikschouten.cleanarchitecture.password
import com.erikschouten.cleanarchitecture.repositories.UserRepository
import com.erikschouten.cleanarchitecture.usecases.Mutation
import com.erikschouten.cleanarchitecture.usecases.UsecaseA1

@Mutation
class CreateUser(
    private val repository: UserRepository,
    private val userExists: UserExists
) : UsecaseA1<CreateUserModel, UserModel>(CreateUserModel::class, UserModel::class) {

    override val executor = { authentication: UserModel?, a0: CreateUserModel ->
        if (authentication == null || !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        if (!email(a0.email)) throw EmailInvalidException()
        if (!password(a0.password)) throw PasswordInvalidException()
        if (userExists(authentication, a0.email)) throw EmailAlreadyExistsException()
        /** TODO: BCrypt **/
        UserModel.of(repository.save(a0.toUser()))
    }
}

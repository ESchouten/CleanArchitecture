package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.LoginException
import com.erikschouten.cleanarchitecture.dependency.Authenticator
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.entity.Email
import com.erikschouten.cleanarchitecture.entity.Password
import com.erikschouten.cleanarchitecture.entity.PasswordHash
import com.erikschouten.cleanarchitecture.model.LoginUserModel
import com.erikschouten.cleanarchitecture.model.UserModel
import com.erikschouten.cleanarchitecture.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecase.Query
import com.erikschouten.cleanarchitecture.usecase.UsecaseA1

@Query
class LoginUser(
    private val repository: UserRepository,
    private val authenticator: Authenticator,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<LoginUserModel, String>(LoginUserModel::class, String::class) {

    override val authenticated = false
    override val executor = { _: UserModel?, a0: LoginUserModel ->
        val user = repository.findByEmail(Email(a0.email))
        if (user == null || !passwordEncoder.matches(Password(a0.password), user.password)) throw LoginException()
        authenticator.generate(user.id)
    }
}

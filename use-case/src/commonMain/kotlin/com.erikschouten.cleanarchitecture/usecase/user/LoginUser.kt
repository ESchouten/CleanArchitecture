package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.LoginException
import com.erikschouten.cleanarchitecture.Authenticator
import com.erikschouten.cleanarchitecture.PasswordEncoder
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

    override val executor = { _: UserModel?, a0: LoginUserModel ->
        val user = repository.findByEmail(a0.email)
        if (user == null || !passwordEncoder.matches(a0.password, user.password)) throw LoginException()
        authenticator.generate(user.id)
    }
}

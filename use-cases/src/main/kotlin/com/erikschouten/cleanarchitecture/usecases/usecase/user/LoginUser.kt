package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.LoginUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1

@Query
class LoginUser(
    private val repository: UserRepository,
    private val authenticator: Authenticator,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<LoginUserModel, String>(LoginUserModel::class, String::class) {

    override val authenticated = false
    override val executor: suspend (UserModel?, LoginUserModel) -> String = { _, a0 ->
        val user = repository.findByEmail(a0.email)
        if (user == null || !passwordEncoder.matches(a0.password, user.password)) throw LoginException()
        authenticator.generate(user.id)
    }
}

package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.UserNotFoundException
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.ChangePasswordModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Mutation
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
@Mutation
class ChangePassword(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangePasswordModel, UserModel>(typeOf<ChangePasswordModel>(), typeOf<UserModel>()) {

    override val authorities = emptyList<Authorities>()
    override val executor: suspend (UserModel?, ChangePasswordModel) -> UserModel = { authentication, a0 ->
        if (authentication!!.id != a0.id && !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        val user = repository.findById(a0.id) ?: throw UserNotFoundException()
        UserModel(repository.update(user.copy(password = passwordEncoder.encode(a0.password))))
    }
}

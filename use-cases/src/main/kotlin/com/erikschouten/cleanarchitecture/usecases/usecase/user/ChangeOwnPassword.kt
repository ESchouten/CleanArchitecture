package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.ChangeOwnPasswordModel
import com.erikschouten.cleanarchitecture.usecases.model.ChangePasswordModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Mutation
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
@Mutation
class ChangeOwnPassword(
    private val repository: UserRepository,
    private val changePassword: ChangePassword,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangeOwnPasswordModel, UserModel>(ChangeOwnPasswordModel::class, typeOf<UserModel>()) {

    override val authorities = emptyList<Authorities>()
    override val executor: suspend (UserModel?, ChangeOwnPasswordModel) -> UserModel = { authentication, a0 ->
        val user = repository.findById(authentication!!.id) ?: throw AuthorizationException()
        if (!passwordEncoder.matches(a0.current, user.password)) throw AuthorizationException()
        changePassword(authentication, ChangePasswordModel(authentication.id, a0.password))
    }
}

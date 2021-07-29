package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
@Query
class ListUsers(
    private val repository: UserRepository,
) : UsecaseA0<List<UserModel>>(typeOf<List<UserModel>>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?) -> List<UserModel> = { _ ->
        repository.findAll().map { UserModel(it) }
    }
}

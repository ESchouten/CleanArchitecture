package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class GetUser(
    private val repository: UserRepository
) : UsecaseA1<Int, UserModel>(typeOf<Int>(), typeOf<UserModel>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, Int) -> UserModel = { _, a0 ->
        repository.findById(a0)?.let { UserModel(it) }!!
    }
}

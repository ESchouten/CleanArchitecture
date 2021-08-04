package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Mutation
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class DeleteUser(
    private val repository: UserRepository,
) : UsecaseA1<Int, Boolean>(typeOf<Int>(), typeOf<Boolean>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, Int) -> Boolean = { _, a0 ->
        repository.delete(a0)
        true
    }
}

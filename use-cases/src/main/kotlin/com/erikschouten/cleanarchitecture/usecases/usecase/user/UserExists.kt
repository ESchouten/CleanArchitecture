package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1

@Query
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<Email, Boolean>(Email::class, Boolean::class) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, Email) -> Boolean = { _, a0 ->
        repository.findByEmail(a0) != null
    }
}

package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA1

@Query
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<Email, Boolean>(Email::class, Boolean::class) {

    override val executor = { _: UserModel?, a0: Email ->
        repository.findByEmail(a0) != null
    }
}

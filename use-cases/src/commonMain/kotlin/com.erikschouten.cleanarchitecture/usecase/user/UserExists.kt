package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.LoginException
import com.erikschouten.cleanarchitecture.entity.Email
import com.erikschouten.cleanarchitecture.model.UserModel
import com.erikschouten.cleanarchitecture.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecase.Query
import com.erikschouten.cleanarchitecture.usecase.UsecaseA1

@Query
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<Email, Boolean>(Email::class, Boolean::class) {

    override val executor = { _: UserModel?, a0: Email ->
        repository.findByEmail(a0) != null
    }
}

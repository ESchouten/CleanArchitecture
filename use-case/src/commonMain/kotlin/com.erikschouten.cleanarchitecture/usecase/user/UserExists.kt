package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.LoginException
import com.erikschouten.cleanarchitecture.model.UserModel
import com.erikschouten.cleanarchitecture.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecase.Query
import com.erikschouten.cleanarchitecture.usecase.UsecaseA1

@Query
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<String, Boolean>(String::class, Boolean::class) {

    override val executor = { authentication: UserModel?, a0: String ->
        if (authentication == null) throw LoginException()
        repository.findByEmail(a0) != null
    }
}

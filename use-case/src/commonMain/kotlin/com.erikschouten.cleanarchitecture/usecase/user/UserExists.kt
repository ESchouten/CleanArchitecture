package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.model.UserModel
import com.erikschouten.cleanarchitecture.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecase.Query
import com.erikschouten.cleanarchitecture.usecase.UsecaseA1

@Query
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<String, Boolean>(String::class, Boolean::class) {

    override val executor = { _: UserModel?, a0: String ->
        repository.findByEmail(a0) != null
    }
}

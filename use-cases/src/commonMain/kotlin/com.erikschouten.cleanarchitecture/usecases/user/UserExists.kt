package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.models.UserModel
import com.erikschouten.cleanarchitecture.repositories.UserRepository
import com.erikschouten.cleanarchitecture.usecases.Usecase
import com.erikschouten.cleanarchitecture.usecases.UsecaseA1

@Usecase
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<String, Boolean>(String::class, Boolean::class) {

    override val executor = { _: UserModel?, a0: String ->
        repository.findByEmail(a0) != null
    }
}

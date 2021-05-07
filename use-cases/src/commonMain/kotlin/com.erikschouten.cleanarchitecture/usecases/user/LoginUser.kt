package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.LoginException
import com.benasher44.uuid.Uuid
import com.erikschouten.cleanarchitecture.models.LoginUserModel
import com.erikschouten.cleanarchitecture.models.UserModel
import com.erikschouten.cleanarchitecture.repositories.UserRepository
import com.erikschouten.cleanarchitecture.usecases.Usecase
import com.erikschouten.cleanarchitecture.usecases.UsecaseA1

@Usecase
class LoginUser(
    private val repository: UserRepository,
    private val encoder: (Uuid) -> String,
) : UsecaseA1<LoginUserModel, String>(LoginUserModel::class, String::class) {

    override val executor = { _: UserModel?, a0: LoginUserModel ->
        val user = repository.findByEmail(a0.email)
        /** TODO: BCrypt **/
        if (user == null || a0.password != user.password) throw LoginException()
        encoder(user.id)
    }
}

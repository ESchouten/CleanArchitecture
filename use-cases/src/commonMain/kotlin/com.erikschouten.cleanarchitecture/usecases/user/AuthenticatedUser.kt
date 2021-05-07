package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.LoginException
import com.erikschouten.cleanarchitecture.models.UserModel
import com.erikschouten.cleanarchitecture.usecases.UsecaseA0

class AuthenticatedUser : UsecaseA0<UserModel>(UserModel::class) {

    override val executor = { authentication: UserModel? ->
        authentication ?: throw LoginException()
    }
}

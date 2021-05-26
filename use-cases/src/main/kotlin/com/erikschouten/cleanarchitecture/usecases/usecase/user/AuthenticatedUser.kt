package com.erikschouten.cleanarchitecture.usecases.usecase.user

import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.Query
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseA0

@Query
class AuthenticatedUser : UsecaseA0<UserModel>(UserModel::class) {

    override val executor: suspend (UserModel?) -> UserModel = { authentication ->
        authentication!!
    }
}

package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.model.UserModel
import com.erikschouten.cleanarchitecture.usecase.Query
import com.erikschouten.cleanarchitecture.usecase.UsecaseA0

@Query
class AuthenticatedUser : UsecaseA0<UserModel>(UserModel::class) {

    override val executor = { authentication: UserModel? ->
        authentication!!
    }
}

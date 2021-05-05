package usecases.user

import LoginException
import models.UserModel
import usecases.Usecase
import usecases.UsecaseA0

@Usecase
class AuthenticatedUser : UsecaseA0<UserModel>(UserModel::class) {

    override val executor = { authentication: UserModel? ->
        authentication ?: throw LoginException()
    }
}

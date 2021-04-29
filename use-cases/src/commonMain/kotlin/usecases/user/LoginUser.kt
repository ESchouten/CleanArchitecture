package usecases.user

import LoginException
import models.LoginUserModel
import models.UserModel
import repositories.UserRepository
import usecases.UseCase

data class LoginUser(
    private val repository: UserRepository
) : UseCase<LoginUserModel, String> {

    override val executor = { request: LoginUserModel, _: UserModel? ->
        val user = repository.findByEmail(request.email)
        /** TODO: BCrypt + JWT **/
        if (user == null || request.password != user.password) throw LoginException()
        user.id.toString()
    }
}

package usecases.user

import LoginException
import com.benasher44.uuid.Uuid
import models.LoginUserModel
import models.UserModel
import repositories.UserRepository
import usecases.UseCase

data class LoginUser(
    private val repository: UserRepository,
    private val encoder: (Uuid) -> String,
) : UseCase<LoginUserModel, String>(LoginUserModel::class, String::class) {

    override val executor = { request: LoginUserModel, _: UserModel? ->
        val user = repository.findByEmail(request.email)
        /** TODO: BCrypt **/
        if (user == null || request.password != user.password) throw LoginException()
        encoder(user.id)
    }
}

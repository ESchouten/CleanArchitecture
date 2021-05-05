package usecases.user

import LoginException
import com.benasher44.uuid.Uuid
import models.LoginUserModel
import models.UserModel
import repositories.UserRepository
import usecases.Usecase
import usecases.UsecaseA1

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

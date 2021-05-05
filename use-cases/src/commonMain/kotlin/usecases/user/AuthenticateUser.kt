package usecases.user

import LoginException
import com.benasher44.uuid.Uuid
import models.UserModel
import repositories.UserRepository
import usecases.Usecase
import usecases.UsecaseA1

@Usecase
class AuthenticateUser(
    private val repository: UserRepository,
) : UsecaseA1<Uuid, UserModel>(Uuid::class, UserModel::class) {

    override val executor = { _: UserModel?, a0: Uuid ->
        repository.findById(a0)?.let { UserModel.of(it) } ?: throw LoginException()
    }
}

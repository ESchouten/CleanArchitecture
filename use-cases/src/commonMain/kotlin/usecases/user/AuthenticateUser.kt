package usecases.user

import LoginException
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import models.LoginUserModel
import models.UserModel
import repositories.UserRepository
import usecases.UseCase

data class AuthenticateUser(
    private val repository: UserRepository,
) : UseCase<Uuid, UserModel>(Uuid::class, UserModel::class) {

    override val executor = { request: Uuid, _: UserModel? ->
        repository.findById(request)?.let { UserModel.of(it) } ?: throw LoginException()
    }
}

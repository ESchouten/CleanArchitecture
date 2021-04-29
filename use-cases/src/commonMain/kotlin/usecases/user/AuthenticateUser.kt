package usecases.user

import LoginException
import com.benasher44.uuid.uuidFrom
import models.LoginUserModel
import models.UserModel
import repositories.UserRepository
import usecases.UseCase

data class AuthenticateUser(
    private val repository: UserRepository
) : UseCase<String, UserModel> {

    override val executor = { request: String, _: UserModel? ->
        /** TODO: JWT **/
        repository.findById(uuidFrom(request))?.let { UserModel.of(it) } ?: throw LoginException()
    }
}

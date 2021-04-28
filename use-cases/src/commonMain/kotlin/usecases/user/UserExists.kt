package usecases.user

import models.UserModel
import repositories.UserRepository
import usecases.UseCase

data class UserExists(
    private val repository: UserRepository
) : UseCase<String, Boolean> {
    override val executor = { request: String, _: UserModel? ->
        repository.findByEmail(request) != null
    }
}

package usecases.user

import models.UserModel
import repositories.UserRepository
import usecases.UseCase

data class UserExists(
    private val repository: UserRepository
) : UseCase<String, Boolean>(String::class, Boolean::class) {
    override val executor = { request: String, _: UserModel? ->
        repository.findByEmail(request) != null
    }
}

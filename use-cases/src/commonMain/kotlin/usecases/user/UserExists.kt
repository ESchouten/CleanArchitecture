package usecases.user

import models.UserModel
import repositories.UserRepository
import usecases.Usecase
import usecases.UsecaseA1

@Usecase
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<String, Boolean>(String::class, Boolean::class) {

    override val executor = { _: UserModel?, a0: String, ->
        repository.findByEmail(a0) != null
    }
}

package usecases.usecase.user

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.model.UserModel
import usecases.usecase.Read
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Read
class UserExists(
    logger: Logger,
    private val repository: UserRepository
) : UsecaseA1<Email, Boolean>(typeOf<Email>(), typeOf<Boolean>(), logger) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, email: Email): Boolean {
        return repository.findByEmail(email) != null
    }
}

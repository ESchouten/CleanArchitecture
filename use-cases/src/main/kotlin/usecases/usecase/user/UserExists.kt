package usecases.usecase.user

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class UserExists(
    private val repository: UserRepository
) : UsecaseA1<Email, Boolean>(typeOf<Email>(), typeOf<Boolean>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, Email) -> Boolean = { _, a0 ->
        repository.findByEmail(a0) != null
    }
}

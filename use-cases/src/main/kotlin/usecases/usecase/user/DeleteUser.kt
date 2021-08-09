package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class DeleteUser(
    private val repository: UserRepository,
) : UsecaseA1<Int, Boolean>(typeOf<Int>(), typeOf<Boolean>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, Int) -> Boolean = { _, a0 ->
        repository.delete(a0)
        true
    }
}

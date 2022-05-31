package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class DeleteUser(
    logger: Logger,
    private val repository: UserRepository,
) : UsecaseA1<Int, Boolean>(typeOf<Int>(), typeOf<Boolean>(), logger) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, a0: Int): Boolean {
        repository.delete(a0)
        return true
    }
}

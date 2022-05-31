package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class GetUser(
    logger: Logger,
    private val repository: UserRepository
) : UsecaseA1<Int, UserModel>(typeOf<Int>(), typeOf<UserModel>(), logger) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, a0: Int): UserModel {
        return repository.findById(a0)?.let { UserModel(it) }!!
    }
}

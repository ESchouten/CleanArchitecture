package usecases.usecase.user

import domain.EmailAlreadyExistsException
import domain.UserNotFoundException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class UpdateUser(
    private val repository: UserRepository,
    private val userExists: UserExists,
) : UsecaseA1<UserModel, UserModel>(typeOf<UserModel>(), typeOf<UserModel>()) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, a0: UserModel): UserModel {
        val old = repository.findById(a0.id) ?: throw UserNotFoundException()
        if (old.email != a0.email && userExists(authentication, a0.email)) throw EmailAlreadyExistsException()
        return UserModel(repository.update(a0.toUser(old.password)))
    }
}

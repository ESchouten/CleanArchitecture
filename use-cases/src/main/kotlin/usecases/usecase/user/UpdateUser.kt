package usecases.usecase.user

import domain.EmailAlreadyExistsException
import domain.UserNotFoundException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.model.UpdateUserModel
import usecases.model.UserModel
import usecases.usecase.Update
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Update
class UpdateUser(
    logger: Logger,
    private val repository: UserRepository,
    private val userExists: UserExists,
) : UsecaseA1<UpdateUserModel, UserModel>(typeOf<UpdateUserModel>(), typeOf<UserModel>(), logger) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, user: UpdateUserModel): UserModel {
        val old = repository.findById(user.id) ?: throw UserNotFoundException()
        if (old.email != user.email && userExists(authentication, user.email)) throw EmailAlreadyExistsException()
        return UserModel(repository.update(user.toUser(old.password)))
    }
}

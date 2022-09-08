package usecases.usecase.user

import domain.EmailAlreadyExistsException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder
import usecases.model.CreateUserModel
import usecases.model.UserModel
import usecases.usecase.Create
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Create
class CreateUser(
    logger: Logger,
    private val repository: UserRepository,
    private val userExists: UserExists,
    private val passwordEncoder: PasswordEncoder,
) : UsecaseA1<CreateUserModel, UserModel>(typeOf<CreateUserModel>(), typeOf<UserModel>(), logger) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, user: CreateUserModel): UserModel {
        if (userExists(authentication, user.email)) throw EmailAlreadyExistsException()
        return UserModel(repository.create(user.toUser(passwordEncoder)))
    }
}

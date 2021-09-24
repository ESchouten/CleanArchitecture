package usecases.usecase.user

import domain.EmailAlreadyExistsException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.PasswordEncoder
import usecases.model.CreateUserModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class CreateUser(
    private val repository: UserRepository,
    private val userExists: UserExists,
    private val passwordEncoder: PasswordEncoder,
) : UsecaseA1<CreateUserModel, UserModel>(typeOf<CreateUserModel>(), typeOf<UserModel>()) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, a0: CreateUserModel): UserModel {
        if (userExists(authentication, a0.email)) throw EmailAlreadyExistsException()
        return UserModel(repository.create(a0.toUser(passwordEncoder)))
    }
}

package usecases.usecase.user

import domain.EmailAlreadyExistsException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.usecase.dependency.PasswordEncoder
import usecases.usecase.model.CreateUserModel
import usecases.usecase.model.UserModel
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
    override val executor: suspend (UserModel?, CreateUserModel) -> UserModel = { authentication, a0 ->
        if (userExists(authentication, a0.email)) throw EmailAlreadyExistsException()
        UserModel(repository.create(a0.toUser(passwordEncoder)))
    }
}

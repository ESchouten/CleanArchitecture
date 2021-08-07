package usecases.usecase.user

import domain.EmailAlreadyExistsException
import domain.UserNotFoundException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.usecase.model.UpdateUserModel
import usecases.usecase.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class UpdateUser(
    private val repository: UserRepository,
    private val userExists: UserExists,
) : UsecaseA1<UpdateUserModel, UserModel>(typeOf<UpdateUserModel>(), typeOf<UserModel>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, UpdateUserModel) -> UserModel = { authentication, a0 ->
        val old = repository.findById(a0.id) ?: throw UserNotFoundException()
        if (old.email != a0.email && userExists(authentication, a0.email)) throw EmailAlreadyExistsException()
        UserModel(repository.update(a0.toUser(old.password)))
    }
}

package usecases.usecase.user

import domain.AuthorizationException
import domain.UserNotFoundException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.usecase.dependency.PasswordEncoder
import usecases.usecase.model.ChangePasswordModel
import usecases.usecase.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class ChangePassword(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangePasswordModel, UserModel>(typeOf<ChangePasswordModel>(), typeOf<UserModel>()) {

    override val authorities = emptyList<Authorities>()
    override val executor: suspend (UserModel?, ChangePasswordModel) -> UserModel = { authentication, a0 ->
        if (authentication!!.id != a0.id && !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        val user = repository.findById(a0.id) ?: throw UserNotFoundException()
        UserModel(repository.update(user.copy(password = passwordEncoder.encode(a0.password))))
    }
}

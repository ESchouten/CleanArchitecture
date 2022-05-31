package usecases.usecase.user

import domain.AuthorizationException
import domain.UserNotFoundException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder
import usecases.model.ChangePasswordModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class ChangePassword(
    logger: Logger,
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangePasswordModel, UserModel>(typeOf<ChangePasswordModel>(), typeOf<UserModel>(), logger) {

    override val authorities = emptyList<Authorities>()
    override suspend fun executor(authentication: UserModel?, a0: ChangePasswordModel): UserModel {
        if (authentication!!.id != a0.id && !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        val user = repository.findById(a0.id) ?: throw UserNotFoundException()
        return UserModel(repository.update(user.copy(password = passwordEncoder.encode(a0.password))))
    }
}

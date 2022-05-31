package usecases.usecase.user

import domain.AuthorizationException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder
import usecases.model.ChangeOwnPasswordModel
import usecases.model.ChangePasswordModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class ChangeOwnPassword(
    logger: Logger,
    private val repository: UserRepository,
    private val changePassword: ChangePassword,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangeOwnPasswordModel, UserModel>(typeOf<ChangeOwnPasswordModel>(), typeOf<UserModel>(), logger) {

    override val authorities = emptyList<Authorities>()
    override suspend fun executor(authentication: UserModel?, a0: ChangeOwnPasswordModel): UserModel {
        val user = repository.findById(authentication!!.id) ?: throw AuthorizationException()
        if (!passwordEncoder.matches(a0.current, user.password)) throw AuthorizationException()
        return changePassword(authentication, ChangePasswordModel(authentication.id, a0.password))
    }
}

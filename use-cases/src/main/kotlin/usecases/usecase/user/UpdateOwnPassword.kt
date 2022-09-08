package usecases.usecase.user

import domain.AuthorizationException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder
import usecases.model.ChangeOwnPasswordModel
import usecases.model.ChangePasswordModel
import usecases.model.UserModel
import usecases.usecase.Update
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Update
class UpdateOwnPassword(
    logger: Logger,
    private val repository: UserRepository,
    private val updatePassword: UpdatePassword,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangeOwnPasswordModel, UserModel>(typeOf<ChangeOwnPasswordModel>(), typeOf<UserModel>(), logger) {

    override val authorities = emptyList<Authorities>()
    override suspend fun executor(authentication: UserModel?, passwords: ChangeOwnPasswordModel): UserModel {
        val user = repository.findById(authentication!!.id) ?: throw AuthorizationException()
        if (!passwordEncoder.matches(passwords.current, user.password)) throw AuthorizationException()
        return updatePassword(authentication, ChangePasswordModel(authentication.id, passwords.password))
    }
}

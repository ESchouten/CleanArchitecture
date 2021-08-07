package usecases.usecase.user

import domain.AuthorizationException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.usecase.dependency.PasswordEncoder
import usecases.usecase.model.ChangeOwnPasswordModel
import usecases.usecase.model.ChangePasswordModel
import usecases.usecase.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class ChangeOwnPassword(
    private val repository: UserRepository,
    private val changePassword: ChangePassword,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<ChangeOwnPasswordModel, UserModel>(typeOf<ChangeOwnPasswordModel>(), typeOf<UserModel>()) {

    override val authorities = emptyList<Authorities>()
    override val executor: suspend (UserModel?, ChangeOwnPasswordModel) -> UserModel = { authentication, a0 ->
        val user = repository.findById(authentication!!.id) ?: throw AuthorizationException()
        if (!passwordEncoder.matches(a0.current, user.password)) throw AuthorizationException()
        changePassword(authentication, ChangePasswordModel(authentication.id, a0.password))
    }
}

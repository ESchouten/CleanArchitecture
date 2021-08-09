package usecases.usecase.user

import domain.LoginException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Authenticator
import usecases.dependency.PasswordEncoder
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class LoginUser(
    private val repository: UserRepository,
    private val authenticator: Authenticator,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<LoginUserModel, String>(typeOf<LoginUserModel>(), typeOf<String>()) {

    override val authenticated = false
    override val authorities = emptyList<Authorities>()
    override val executor: suspend (UserModel?, LoginUserModel) -> String = { _, a0 ->
        val user = repository.findByEmail(a0.email)
        if (user == null || !passwordEncoder.matches(a0.password, user.password)) throw LoginException()
        authenticator.generate(user.id)
    }
}

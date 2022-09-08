package usecases.usecase.user

import domain.LoginException
import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.dependency.Authenticator
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.Create
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Create
class LoginUser(
    logger: Logger,
    private val repository: UserRepository,
    private val authenticator: Authenticator,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<LoginUserModel, String>(typeOf<LoginUserModel>(), typeOf<String>(), logger) {

    override val authenticated = false
    override val authorities = emptyList<Authorities>()
    override suspend fun executor(authentication: UserModel?, login: LoginUserModel): String {
        val user = repository.findByEmail(login.email)
        if (user == null || user.locked || !passwordEncoder.matches(
                login.password,
                user.password
            )
        ) throw LoginException()
        return "Bearer " + authenticator.generate(UserModel(user))
    }
}

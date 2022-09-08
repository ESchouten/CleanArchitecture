package usecases.usecase.user

import domain.entity.user.Authorities
import usecases.dependency.Logger
import usecases.model.UserModel
import usecases.usecase.Read
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Read
class AuthenticatedUser(logger: Logger) : UsecaseA0<UserModel>(typeOf<UserModel>(), logger) {

    override val authorities = emptyList<Authorities>()
    override suspend fun executor(authentication: UserModel?): UserModel {
        return authentication!!
    }
}

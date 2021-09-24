package usecases.usecase.user

import domain.entity.user.Authorities
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Query
class AuthenticatedUser : UsecaseA0<UserModel>(typeOf<UserModel>()) {

    override val authorities = emptyList<Authorities>()
    override suspend fun executor(authentication: UserModel?): UserModel {
        return authentication!!
    }
}

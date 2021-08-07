package usecases.usecase.user

import domain.entity.user.Authorities
import usecases.usecase.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Query
class AuthenticatedUser : UsecaseA0<UserModel>(typeOf<UserModel>()) {

    override val authorities = emptyList<Authorities>()
    override val executor: suspend (UserModel?) -> UserModel = { authentication ->
        authentication!!
    }
}

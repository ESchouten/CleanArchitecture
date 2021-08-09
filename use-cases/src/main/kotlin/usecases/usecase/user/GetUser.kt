package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class GetUser(
    private val repository: UserRepository
) : UsecaseA1<Int, UserModel>(typeOf<Int>(), typeOf<UserModel>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, Int) -> UserModel = { _, a0 ->
        repository.findById(a0)?.let { UserModel(it) }!!
    }
}

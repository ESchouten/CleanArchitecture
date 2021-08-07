package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.usecase.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Query
class ListUsers(
    private val repository: UserRepository,
) : UsecaseA0<List<UserModel>>(typeOf<List<UserModel>>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?) -> List<UserModel> = { _ ->
        repository.findAll().map { UserModel(it) }
    }
}

package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Query
class ListUsers(
    private val repository: UserRepository,
) : UsecaseA0<List<UserModel>>(typeOf<List<UserModel>>()) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?): List<UserModel> {
        return repository.findAll().map { UserModel(it) }
    }
}

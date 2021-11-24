package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.Pagination
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.model.UserPaginationResult
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class ListUsers(
    private val repository: UserRepository,
) : UsecaseA1<Pagination, UserPaginationResult>(typeOf<Pagination>(), typeOf<UserPaginationResult>()) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, a0: Pagination): UserPaginationResult {
        return UserPaginationResult(repository.findAll(a0))
    }
}

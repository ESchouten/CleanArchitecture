package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repository.Pagination
import domain.repository.UserRepository
import usecases.dependency.Logger
import usecases.model.UserModel
import usecases.model.UserPaginationResult
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class ListUsers(
    logger: Logger,
    private val repository: UserRepository,
) : UsecaseA1<Pagination, UserPaginationResult>(typeOf<Pagination>(), typeOf<UserPaginationResult>(), logger) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, pagination: Pagination): UserPaginationResult {
        return UserPaginationResult(repository.findAll(pagination))
    }
}

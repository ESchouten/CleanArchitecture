package usecases.usecase.user

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.repository.UserRepository
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class FindUsers(
    private val repository: UserRepository,
) : UsecaseA1<List<Email>, List<UserModel>>(typeOf<List<Email>>(), typeOf<List<UserModel>>()) {

    override val authorities = listOf(Authorities.USER)
    override suspend fun executor(authentication: UserModel?, a0: List<Email>): List<UserModel> {
        return repository.findAllByEmails(a0).map { UserModel(it) }
    }
}

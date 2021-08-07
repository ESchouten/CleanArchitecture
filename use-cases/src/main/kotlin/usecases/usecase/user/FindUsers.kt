package usecases.usecase.user

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.repository.UserRepository
import usecases.usecase.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class FindUsers(
    private val repository: UserRepository,
) : UsecaseA1<List<Email>, List<UserModel>>(typeOf<List<Email>>(), typeOf<List<UserModel>>()) {

    override val authorities = listOf(Authorities.USER)
    override val executor: suspend (UserModel?, List<Email>) -> List<UserModel> = { _, emails ->
        repository.findAllByEmails(emails).map { UserModel(it) }
    }
}

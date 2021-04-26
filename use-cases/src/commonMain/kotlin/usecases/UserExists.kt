package usecases

import repositories.UserRepository

data class UserExists(
    private val user: String,
    private val repository: UserRepository
) : UseCase<Boolean> {

    override fun execute(): Boolean {
        return repository.findByEmail(user) != null
    }
}

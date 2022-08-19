package usecases.model

import domain.entity.user.*
import domain.repository.PaginationResult
import usecases.dependency.PasswordEncoder

data class UserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
    val locked: Boolean
) {
    constructor(user: User) : this(user.id, user.email, user.authorities, user.locked)
}

data class UpdateUserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
    val locked: Boolean
) {
    fun toUser(hash: PasswordHash) =
        User(id = id, email = email, authorities = authorities, password = hash, locked = locked)
}

data class ChangeOwnPasswordModel(
    val current: Password,
    val password: NewPassword
)

data class ChangePasswordModel(
    val id: Int,
    val password: NewPassword
)

data class CreateUserModel(
    val email: Email,
    val authorities: List<Authorities>,
    val password: NewPassword,
    val locked: Boolean
) {
    fun toUser(encoder: PasswordEncoder) =
        User(email = email, authorities = authorities, password = encoder.encode(password), locked = locked)
}

data class LoginUserModel(
    val email: Email,
    val password: Password
)

class UserPaginationResult(items: List<UserModel>, total: Long) : PaginationResult<UserModel>(items, total) {
    constructor(pagination: PaginationResult<User>) : this(pagination.items.map { UserModel(it) }, pagination.total)
}

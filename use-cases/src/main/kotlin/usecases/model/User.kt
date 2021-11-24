package usecases.model

import domain.entity.user.*
import domain.repository.PaginationResult
import usecases.dependency.PasswordEncoder

data class UserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
) {
    constructor(user: User) : this(user.id, user.email, user.authorities)
}

data class UpdateUserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
) {
    fun toUser(hash: PasswordHash) = User(email = email, authorities = authorities, password = hash)
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
) {
    fun toUser(encoder: PasswordEncoder) =
        User(email = email, authorities = authorities, password = encoder.encode(password))
}

data class LoginUserModel(
    val email: Email,
    val password: Password,
)

class UserPaginationResult(pagination: PaginationResult<User>) :
    PaginationResult<UserModel>(pagination.transform { UserModel(it) })

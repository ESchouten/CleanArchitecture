package models

import com.benasher44.uuid.Uuid
import entities.Authorities
import entities.User

data class UserModel(
    val id: Uuid,
    val email: String,
    val authorities: List<Authorities>,
) {
    companion object {
        fun of(user: User) = UserModel(user.id, user.email, user.roles)
    }
}

data class CreateUserModel(
    val email: String,
    val authorities: List<Authorities>,
    val password: String,
) {
    fun toUser() {
        fun toUser() = User(email, authorities, password)
    }
}

package dtos

import com.benasher44.uuid.Uuid
import entities.User

data class UserModel(
    val id: Uuid,
    val email: String,
) {
    companion object {
        fun of(user: User) = UserModel(user.id, user.email)
    }
}

data class CreateUserModel(
    val email: String,
    val password: String,
) {
    fun toUser() {
        fun toUser() = User(email, password)
    }
}

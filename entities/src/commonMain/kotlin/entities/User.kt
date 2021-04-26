package entities

data class User(
    val email: String,
    val password: String,
) : UUIDEntity()

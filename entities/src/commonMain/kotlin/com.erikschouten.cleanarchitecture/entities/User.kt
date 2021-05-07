package com.erikschouten.cleanarchitecture.entities

data class User(
    val email: String,
    val roles: List<Authorities>,
    val password: String,
) : UUIDEntity()

enum class Authorities {
    USER
}

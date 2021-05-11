package com.erikschouten.cleanarchitecture.entity

data class User(
    val email: String,
    val roles: List<Authorities>,
    val password: String,
) : UUIDEntity()

enum class Authorities {
    USER
}

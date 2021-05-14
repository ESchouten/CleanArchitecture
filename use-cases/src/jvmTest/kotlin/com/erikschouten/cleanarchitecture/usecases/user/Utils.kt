package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.entity.*
import com.erikschouten.cleanarchitecture.usecases.model.UserModel

val email = Email("erik@erikschouten.com")
val password = Password("P@ssw0rd!")
val passwordHash = PasswordHash(password.value.reversed())
val user = User(email, listOf(Authorities.USER), passwordHash)
val userModel = UserModel(user)

package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.entity.*
import com.erikschouten.cleanarchitecture.model.UserModel

val email = Email("erik@erikschouten.com")
val password = Password("P@ssw0rd!")
val user = User(email, listOf(Authorities.USER), PasswordHash(password.value.reversed()))
val userModel = UserModel(user)

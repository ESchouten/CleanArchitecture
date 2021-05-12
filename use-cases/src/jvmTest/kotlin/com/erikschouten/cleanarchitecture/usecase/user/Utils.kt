package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.entity.Authorities
import com.erikschouten.cleanarchitecture.entity.User
import com.erikschouten.cleanarchitecture.model.UserModel

val email = "erik@erikschouten.com"
val password = "P@ssw0rd!"
val user = User(email, listOf(Authorities.USER), password.reversed())
val userModel = UserModel(user)

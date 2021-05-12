package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.entity.Authorities
import com.erikschouten.cleanarchitecture.entity.Email
import com.erikschouten.cleanarchitecture.entity.Password
import com.erikschouten.cleanarchitecture.entity.User
import com.erikschouten.cleanarchitecture.model.UserModel

val email = "erik@erikschouten.com"
val password = "P@ssw0rd!"
val user = User(Email(email), listOf(Authorities.USER), Password(password.reversed()))
val userModel = UserModel(user)

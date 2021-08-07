package usecases.user

import domain.entity.user.*
import usecases.usecase.model.UserModel

val email = Email("erik@erikschouten.com")
val password = Password("P@ssw0rd!")
val passwordHash = PasswordHash(password.value.reversed())
val id = 1
val user = User(id = id, email = email, authorities = listOf(Authorities.USER), password = passwordHash)
val userModel = UserModel(user)

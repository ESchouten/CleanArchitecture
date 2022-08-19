package usecases.usecase.user

import domain.entity.Email
import domain.entity.Password
import domain.entity.PasswordHash
import domain.entity.user.Authorities
import domain.entity.user.User
import usecases.model.UserModel

val email = Email("erik@erikschouten.com")
val password = Password("P@ssw0rd!")
val passwordHash = PasswordHash(password.value.reversed())
val id = 1
val user = User(id = id, email = email, authorities = listOf(Authorities.USER), password = passwordHash, locked = true)
val userModel = UserModel(user)

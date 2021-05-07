package com.erikschouten.cleanarchitecture

import com.erikschouten.cleanarchitecture.entities.Authorities
import com.erikschouten.cleanarchitecture.entities.User
import io.ktor.application.*
import org.koin.dsl.module
import com.erikschouten.cleanarchitecture.repositories.UserRepository
import com.erikschouten.cleanarchitecture.usecases.user.*
import com.erikschouten.cleanarchitecture.repositories.UserRepositoryImpl

fun Application.config() = environment.config.run {
    Config(
        property("ktor.jwt.domain").getString(),
        property("ktor.jwt.audience").getString(),
        property("ktor.jwt.realm").getString(),
        property("ktor.development").getString().toBoolean(),
    )
}

data class Config(
    val jwtDomain: String,
    val jwtAudience: String,
    val jwtRealm: String,
    val development: Boolean,
)

fun userModule(authenticator: Authenticator) = module {
    single { AuthenticateUser(get()) }
    single { AuthenticatedUser() }
    single { CreateUser(get()) }
    single { LoginUser(get(), authenticator::encode) }
    single { UserExists(get()) }
    single<UserRepository> { UserRepositoryImpl() }
}

fun setup(userRepository: UserRepository) {
    userRepository.save(User("erik@erikschouten.com", listOf(Authorities.USER), "pass"))
}

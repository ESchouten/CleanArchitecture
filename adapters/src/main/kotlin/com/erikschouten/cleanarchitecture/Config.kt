package com.erikschouten.cleanarchitecture

import com.erikschouten.cleanarchitecture.entities.Authorities
import com.erikschouten.cleanarchitecture.entities.User
import io.ktor.application.*
import com.erikschouten.cleanarchitecture.repositories.UserRepository
import com.erikschouten.cleanarchitecture.usecases.user.*
import com.erikschouten.cleanarchitecture.repositories.UserRepositoryImpl
import org.koin.dsl.module
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy

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

fun userModule(config: Config) = module {
    single<AuthenticatedUser>()
    single<AuthenticateUser>()
    single<Authenticator> { AuthenticatorImpl(config) }
    single<CreateUser>()
    single<LoginUser>()
    single<UserExists>()
    singleBy<UserRepository, UserRepositoryImpl>()
}

fun setup(userRepository: UserRepository) {
    userRepository.save(User("erik@erikschouten.com", listOf(Authorities.USER), "pass"))
}

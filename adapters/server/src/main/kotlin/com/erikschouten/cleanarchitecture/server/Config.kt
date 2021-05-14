package com.erikschouten.cleanarchitecture.server

import com.erikschouten.cleanarchitecture.server.auth.AuthenticatorImpl
import com.erikschouten.cleanarchitecture.server.auth.PasswordEncoderImpl
import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.repositories.UserRepositoryImpl
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import io.ktor.application.*
import com.erikschouten.cleanarchitecture.usecases.usecase.user.AuthenticatedUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.LoginUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import org.koin.core.module.Module
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

fun modules(config: Config): List<Module> {
    return listOf(userModule(config))
}

private fun userModule(config: Config) = module {
    single<AuthenticatedUser>()
    single<Authenticator> { AuthenticatorImpl(config) }
    single<CreateUser>()
    single<LoginUser>()
    singleBy<PasswordEncoder, PasswordEncoderImpl>()
    single<UserExists>()
    singleBy<UserRepository, UserRepositoryImpl>()
}

fun setup(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
    userRepository.save(User(Email("erik@erikschouten.com"), listOf(Authorities.USER), passwordEncoder.encode(Password("P@ssw0rd!"))))
}

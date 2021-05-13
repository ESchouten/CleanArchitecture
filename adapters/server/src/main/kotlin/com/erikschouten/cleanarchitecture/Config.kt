package com.erikschouten.cleanarchitecture

import com.erikschouten.cleanarchitecture.auth.AuthenticatorImpl
import com.erikschouten.cleanarchitecture.auth.PasswordEncoderImpl
import com.erikschouten.cleanarchitecture.dependency.Authenticator
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.entity.Authorities
import com.erikschouten.cleanarchitecture.entity.Email
import com.erikschouten.cleanarchitecture.entity.Password
import com.erikschouten.cleanarchitecture.entity.User
import io.ktor.application.*
import com.erikschouten.cleanarchitecture.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecase.user.AuthenticatedUser
import com.erikschouten.cleanarchitecture.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecase.user.LoginUser
import com.erikschouten.cleanarchitecture.usecase.user.UserExists
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

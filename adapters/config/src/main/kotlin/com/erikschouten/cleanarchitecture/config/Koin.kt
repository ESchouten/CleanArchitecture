package com.erikschouten.cleanarchitecture.config

import com.erikschouten.cleanarchitecture.authentication.JWTAuthenticatorImpl
import com.erikschouten.cleanarchitecture.authentication.PasswordEncoderImpl
import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.repositories.UserRepositoryImpl
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.usecase.user.AuthenticatedUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.LoginUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.create
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy

fun modules(config: Config): List<Module> {
    return listOf(userModule(config))
}

@Suppress("USELESS_CAST")
private fun userModule(config: Config) = module {
    single<AuthenticatedUser>()
    single<Authenticator> { JWTAuthenticatorImpl(config.jwt.domain, config.jwt.audience, config.jwt.realm) }
    single<CreateUser>()
    single<LoginUser>()
    singleBy<PasswordEncoder, PasswordEncoderImpl>()
    single<UserExists>()
    single {
        create(
            when (config.database) {
                Database.LOCAL -> UserRepositoryImpl::class
                Database.EXPOSED -> TODO()
            }
        ) as UserRepository
    }
}

fun setup(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
    userRepository.save(
        User(
            Email("erik@erikschouten.com"),
            listOf(Authorities.USER),
            passwordEncoder.encode(Password("P@ssw0rd!"))
        )
    )
}

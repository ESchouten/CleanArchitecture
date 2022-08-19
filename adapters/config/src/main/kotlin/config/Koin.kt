package config

import authentication.JWTAuthenticatorImpl
import authentication.PasswordEncoderImpl
import domain.entity.Email
import domain.entity.Password
import domain.entity.user.Authorities
import domain.entity.user.User
import domain.repository.UserRepository
import logging.LoggerImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repositories.user.InMemoryUserRepository
import repositories.user.UserRepositoryImpl
import usecases.dependency.Authenticator
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder

fun modules(config: Config): List<Module> {
    return listOf(
        commonModule(),
        userModule(config)
    )
}

private fun commonModule() = module {
    singleOf(::LoggerImpl).bind<Logger>()
}

private fun userModule(config: Config) = module {
    val excludeRepo = when (config.database) {
        DatabaseType.LOCAL -> UserRepositoryImpl::class
        DatabaseType.JDBC -> InMemoryUserRepository::class
    }
    usecasesAndRepositories("user", excludeRepositories = listOf(excludeRepo))

    single<Authenticator> {
        JWTAuthenticatorImpl(
            issuer = config.jwt.domain,
            secret = config.jwt.secret
        )
    }
    singleOf(::PasswordEncoderImpl).bind<PasswordEncoder>()
}

suspend fun setup(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
    if (userRepository.count() == 0L) {
        userRepository.create(
            User(
                email = Email("erik@erikschouten.com"),
                authorities = listOf(Authorities.USER),
                password = passwordEncoder.encode(Password("P@ssw0rd!")),
                locked = false
            )
        )
        userRepository.create(
            User(
                email = Email("schouten@erikschouten.com"),
                authorities = emptyList(),
                password = passwordEncoder.encode(Password("P@ssw0rd!")),
                locked = true
            )
        )
    }
}

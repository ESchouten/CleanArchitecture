package com.erikschouten.cleanarchitecture.config

import com.erikschouten.cleanarchitecture.authentication.JWTAuthenticatorImpl
import com.erikschouten.cleanarchitecture.authentication.PasswordEncoderImpl
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.Password
import com.erikschouten.cleanarchitecture.domain.entity.user.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.repositories.user.InMemoryUserRepository
import com.erikschouten.cleanarchitecture.repositories.user.UserRepositoryImpl
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.usecase.user.*
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Kind
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.create
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.reflect.full.isSubclassOf

fun modules(config: Config): List<Module> {
    return listOf(userModule(config))
}

@Suppress("USELESS_CAST")
private fun userModule(config: Config) = module {
    single<AuthenticatedUser>()
    single<ChangeOwnPassword>()
    single<ChangePassword>()
    single<CreateUser>()
    single<DeleteUser>()
    single<ListUsers>()
    single<LoginUser>()
    single<UpdateUser>()
    single<UserExists>()

    single {
        create(
            when (config.database) {
                DatabaseType.LOCAL -> InMemoryUserRepository::class
                DatabaseType.JDBC -> UserRepositoryImpl::class
            }
        ) as UserRepository
    }

    single<Authenticator> {
        JWTAuthenticatorImpl(
            issuer = config.jwt.domain,
            realm = config.jwt.realm,
            secret = config.jwt.secret
        )
    }
    singleBy<PasswordEncoder, PasswordEncoderImpl>()
}

suspend fun setup(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
    val email = Email("erik@erikschouten.com")
    if (userRepository.findByEmail(email) == null) {
        userRepository.create(
            User(
                email = email,
                authorities = listOf(Authorities.USER),
                password = passwordEncoder.encode(Password("P@ssw0rd!"))
            )
        )
    }
}

@KoinInternalApi
inline fun <reified T : Any> getAll(): Collection<T> =
    getKoin().let { koin ->
        koin.getRootScope()._scopeDefinition.definitions.toList()
            .filter { it.kind == Kind.Single }
            .filter { it.primaryType.isSubclassOf(T::class) }
            .map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) }
    }

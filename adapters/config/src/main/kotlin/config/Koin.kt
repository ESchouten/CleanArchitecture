package config

import authentication.JWTAuthenticatorImpl
import authentication.PasswordEncoderImpl
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.Password
import domain.entity.user.User
import domain.repository.UserRepository
import usecases.dependency.Authenticator
import usecases.dependency.PasswordEncoder
import usecases.usecase.user.*
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Kind
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.create
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy
import org.koin.java.KoinJavaComponent.getKoin
import repositories.user.InMemoryUserRepository
import repositories.user.UserRepositoryImpl
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
    single<FindUsers>()
    single<GetUser>()
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

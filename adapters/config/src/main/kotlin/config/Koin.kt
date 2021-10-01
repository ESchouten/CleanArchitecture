package config

import authentication.JWTAuthenticatorImpl
import authentication.PasswordEncoderImpl
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.Password
import domain.entity.user.User
import domain.repository.UserRepository
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Kind
import org.koin.core.instance.newInstance
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single
import org.koin.java.KoinJavaComponent.getKoin
import repositories.user.InMemoryUserRepository
import repositories.user.UserRepositoryImpl
import usecases.dependency.Authenticator
import usecases.dependency.PasswordEncoder
import usecases.usecase.user.*
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

    single { params ->
        newInstance(
            when (config.database) {
                DatabaseType.LOCAL -> InMemoryUserRepository::class
                DatabaseType.JDBC -> UserRepositoryImpl::class
            }, params
        )
    }

    single<Authenticator> {
        JWTAuthenticatorImpl(
            issuer = config.jwt.domain,
            secret = config.jwt.secret
        )
    }
    single<PasswordEncoderImpl>().bind<PasswordEncoder>()
}

suspend fun setup(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
    if (userRepository.count() == 0L) {
        userRepository.create(
            User(
                email = Email("erik@erikschouten.com"),
                authorities = listOf(Authorities.USER),
                password = passwordEncoder.encode(Password("P@ssw0rd!"))
            )
        )
    }
}

@KoinInternalApi
inline fun <reified T : Any> getAll(): Collection<T> =
    getKoin().let { koin ->
        koin.instanceRegistry.instances.values.map { it.beanDefinition }
            .filter { it.kind == Kind.Singleton }
            .filter { it.primaryType.isSubclassOf(T::class) }
            .map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) }
    }

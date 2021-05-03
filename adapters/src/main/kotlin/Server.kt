import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.benasher44.uuid.uuidFrom
import entities.Authorities
import entities.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import models.LoginUserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import repositories.UserRepository
import repositories.UserRepositoryImpl
import usecases.UseCase
import usecases.user.AuthenticateUser
import usecases.user.CreateUser
import usecases.user.LoginUser
import usecases.user.UserExists
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    val authenticator = Authenticator(
        issuer = environment.config.property("ktor.jwt.domain").getString(),
        audience = environment.config.property("ktor.jwt.audience").getString(),
        myRealm = environment.config.property("ktor.jwt.realm").getString(),
    )

    val userModule = module {
        single { AuthenticateUser(get()) }
        single { CreateUser(get()) }
        single { LoginUser(get(), authenticator::encode) }
        single { UserExists(get()) }
        single<UserRepository> { UserRepositoryImpl() }
    }

    install(Koin) {
        modules(userModule)
    }

    val userRepository: UserRepository by inject()
    userRepository.save(User("erik@erikschouten.com", listOf(Authorities.USER), "pass"))

    install(Authentication) {
        jwt {
            verifier(authenticator.verifier)
            validate { credential ->
                if (credential.payload.audience.contains(authenticator.audience)) {
                    val authenticateUser: AuthenticateUser by inject()
                    UserPrincipal(authenticateUser.execute(uuidFrom(credential.payload.subject), null))
                } else {
                    null
                }
            }
        }
    }


    val usecases = listOf<UseCase<*, *>>(LoginUser(getDependency(), authenticator::encode))
    val types = listOf<KClass<*>>(LoginUserModel::class)

    install(GraphQL) {
        playground = true
        wrap {
            authenticate(optional = true, build = it)
        }
        context { call ->
            call.authentication.principal<UserPrincipal>()?.let {
                +it
            }
        }
        schema {
            query("hello") {
                resolver { ctx: Context ->
                    ctx.get<UserPrincipal>()?.email ?: "Unknown"
                }
            }
            query("login") {
                resolver { request: LoginUserModel ->
                    val loginUser: LoginUser by inject()
                    loginUser.execute(request, null)
                }
            }
            usecases.forEach {
                connection(it)
            }

            types.forEach {
                type(it) {}
            }
        }
    }
}

fun <T, U, V: UseCase<T, U>> SchemaBuilder.connection(usecase: V) {
    val resultType = usecase.resultType.createType()
    query(usecase::class.simpleName!!) {
        resolver { request: T, ctx: Context ->
            usecase.execute(request, ctx.get<UserPrincipal>()?.toUserModel())
        }.apply { target.setReturnType(resultType) }
    }
}

inline fun <reified T> getDependency(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

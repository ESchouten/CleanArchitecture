import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.model.InputValueDef
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
import usecases.*
import usecases.user.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
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
        single { AuthenticatedUser() }
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
                    UserPrincipal(authenticateUser.execute(null, uuidFrom(credential.payload.subject)))
                } else {
                    null
                }
            }
        }
    }


    val usecases = listOf<UsecaseType>(LoginUser(getDependency(), authenticator::encode))
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
            usecases.forEach {
                usecase(it)
            }
            types.forEach {
                type(it) {}
            }
        }
    }
}

fun SchemaBuilder.usecase(usecase: UsecaseType) {
    when (usecase) {
        is UsecaseA0<*> -> usecase(usecase)
        is UsecaseA1<*, *> -> usecase(usecase)
        else -> throw Exception("Invalid usecase")
    }
}

fun <T : Any, V : UsecaseA0<T>> SchemaBuilder.usecase(usecase: V) {
    query(usecase::class.simpleName!!) {
        resolver { ctx: Context ->
            usecase.execute(ctx.get<UserPrincipal>()?.toUserModel())
        }.apply {
            target.setReturnType(usecase.result.createType())
        }
    }
}

fun <T : Any, U : Any, V : UsecaseA1<U, T>> SchemaBuilder.usecase(usecase: V) {
    query(usecase::class.simpleName!!) {
        resolver {ctx: Context,  a0: U ->
            usecase.execute(ctx.get<UserPrincipal>()?.toUserModel(), a0)
        }.apply {
            target.setReturnType(usecase.result.createType())
            addInputValues(listOf(InputValueDef(usecase.a0, "a0")))
        }
    }
}

inline fun <reified T> getDependency(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

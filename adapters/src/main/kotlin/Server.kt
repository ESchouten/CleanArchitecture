import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.benasher44.uuid.uuidFrom
import entities.Authorities
import entities.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import models.LoginUserModel
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import repositories.UserRepository
import repositories.UserRepositoryImpl
import usecases.user.AuthenticateUser
import usecases.user.CreateUser
import usecases.user.LoginUser
import usecases.user.UserExists

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
            mutation("login") {
                resolver { request: LoginUserModel ->
                    val loginUser: LoginUser by inject()
                    loginUser.execute(request, null)
                }
            }
            type<LoginUserModel>()
        }
    }

//    routing {
//        get("/") {
//            call.respondText("Hello, world!")
//        }
//    }
}

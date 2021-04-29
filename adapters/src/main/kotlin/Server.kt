import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import repositories.UserRepository
import repositories.UserRepositoryImpl
import usecases.user.AuthenticateUser
import usecases.user.CreateUser
import usecases.user.LoginUser
import usecases.user.UserExists

fun main(args: Array<String>) = EngineMain.main(args)

val userModule = module {
    single { AuthenticateUser(get()) }
    single { CreateUser(get()) }
    single { LoginUser(get()) }
    single { UserExists(get()) }
    single<UserRepository> { UserRepositoryImpl() }
}

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    install(Koin) {
        modules(userModule)
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}

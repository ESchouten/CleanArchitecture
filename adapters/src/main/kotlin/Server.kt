import com.apurebase.kgraphql.GraphQL
import com.benasher44.uuid.uuidFrom
import entities.Authorities
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import models.LoginUserModel
import models.UserModel
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import usecases.UsecaseType
import usecases.user.AuthenticateUser
import usecases.user.AuthenticatedUser
import usecases.user.LoginUser
import kotlin.reflect.KClass

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    val config = config()
    val authenticator = Authenticator(config)

    install(Koin) {
        modules(
            userModule(authenticator)
        )
    }

    install(Authentication) {
        jwt {
            verifier(authenticator.verifier)
            validate { credential ->
                if (credential.payload.audience.contains(authenticator.audience)) {
                    val authenticateUser: AuthenticateUser = get()
                    UserPrincipal(authenticateUser.execute(null, uuidFrom(credential.payload.subject)))
                } else {
                    null
                }
            }
        }
    }

    val usecases = arrayOf<UsecaseType<*>>(
        LoginUser(get(), authenticator::encode),
        AuthenticatedUser(),
    )

    install(GraphQL) {
        configure(usecases, config.development)
    }
    if (config.development) setup(get())
}

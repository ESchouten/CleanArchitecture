import com.apurebase.kgraphql.GraphQL
import com.benasher44.uuid.Uuid
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
import org.koin.ktor.ext.inject
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
                    val authenticateUser: AuthenticateUser by inject()
                    authenticateUser.execute(null, uuidFrom(credential.payload.subject)) as UserPrincipal
                } else {
                    null
                }
            }
        }
    }

    val usecases = listOf<UsecaseType<*>>(
        LoginUser(get(), authenticator::encode),
        AuthenticatedUser(),
    )
    val types = listOf<KClass<*>>(
        LoginUserModel::class,
        UserModel::class,
        Authorities::class,
    )

    install(GraphQL) {
        configure(usecases, types, config.development)
    }
    if (config.development) setup(get())
}

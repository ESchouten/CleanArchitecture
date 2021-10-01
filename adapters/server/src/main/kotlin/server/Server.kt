package server

import authentication.JWTAuthenticatorImpl
import com.apurebase.kgraphql.GraphQL
import config.getAll
import config.modules
import config.setup
import domain.repository.UserRepository
import graphql.usecases
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import usecases.dependency.Authenticator
import usecases.model.UserModel

fun main(args: Array<String>) = EngineMain.main(args)

@KoinInternalApi
@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    val config = config()

    install(Koin) {
        modules(modules(config))
    }

    install(Authentication) {
        jwt {
            val authenticator = get<Authenticator>() as JWTAuthenticatorImpl
            val userRepository = get<UserRepository>()
            verifier(authenticator.verifier)
            validate { credential ->
                userRepository.findById(credential.payload.subject.toInt())?.let {
                    UserPrincipal(UserModel(it))
                }
            }
        }
    }

    install(GraphQL) {
        this.playground = config.development

        wrap {
            authenticate(optional = true, build = it)
        }

        context { call ->
            call.authentication.principal<UserPrincipal>()?.let {
                +it.toUserModel()
            }
        }

        schema(usecases(getAll()))
    }

    launch {
        setup(get(), get())
    }
}

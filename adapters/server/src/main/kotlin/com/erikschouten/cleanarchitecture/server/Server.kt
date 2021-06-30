package com.erikschouten.cleanarchitecture.server

import com.apurebase.kgraphql.GraphQL
import com.erikschouten.cleanarchitecture.authentication.JWTAuthenticatorImpl
import com.erikschouten.cleanarchitecture.config.getAll
import com.erikschouten.cleanarchitecture.config.modules
import com.erikschouten.cleanarchitecture.config.setup
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.graphql.usecases
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinInternalApi
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import java.util.*

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
            realm = authenticator.realm
            validate { credential ->
                if (credential.payload.audience.contains(authenticator.audience)) {
                    userRepository.findById(UUID.fromString(credential.payload.subject))?.let {
                        UserPrincipal(UserModel(it))
                    }
                } else {
                    null
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

    if (config.development) {
        launch {
            setup(get(), get())
        }
    }
}

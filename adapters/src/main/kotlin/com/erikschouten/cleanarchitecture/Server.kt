package com.erikschouten.cleanarchitecture

import com.apurebase.kgraphql.GraphQL
import com.benasher44.uuid.uuidFrom
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import com.erikschouten.cleanarchitecture.usecases.UsecaseType
import com.erikschouten.cleanarchitecture.usecases.user.*
import org.koin.ktor.ext.getKoin

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    val config = config()

    install(Koin) {
        modules(
            userModule(config)
        )
    }

    install(Authentication) {
        jwt {
            val authenticator = get<Authenticator>() as AuthenticatorImpl
            verifier(authenticator.verifier)
            validate { credential ->
                if (credential.payload.audience.contains(authenticator.audience)) {
                    UserPrincipal(get<AuthenticateUser>().execute(null, uuidFrom(credential.payload.subject)))
                } else {
                    null
                }
            }
        }
    }

    val usecases = arrayOf<UsecaseType<*>>(
        get<AuthenticatedUser>(),
        get<AuthenticateUser>(),
        get<CreateUser>(),
        get<LoginUser>(),
        get<UserExists>(),
    )

    install(GraphQL) {
        configure(usecases, config.development)
    }

    if (config.development) setup(get())
}

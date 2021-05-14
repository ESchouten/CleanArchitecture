package com.erikschouten.cleanarchitecture.server

import com.apurebase.kgraphql.GraphQL
import com.benasher44.uuid.uuidFrom
import com.erikschouten.cleanarchitecture.authentication.JWTAuthenticatorImpl
import com.erikschouten.cleanarchitecture.config.modules
import com.erikschouten.cleanarchitecture.config.setup
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.graphql.usecases
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.AuthenticatedUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.LoginUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.server.cio.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get

fun main(args: Array<String>) = EngineMain.main(args)

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
                    userRepository.findById(uuidFrom(credential.payload.subject))?.let {
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

        schema(
            usecases(
                arrayOf(
                    get<AuthenticatedUser>(),
                    get<CreateUser>(),
                    get<LoginUser>(),
                    get<UserExists>()
                )
            )
        )
    }

    if (config.development) setup(get(), get())
}

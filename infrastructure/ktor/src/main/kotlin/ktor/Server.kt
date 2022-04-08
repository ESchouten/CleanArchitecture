package ktor

import com.apurebase.kgraphql.GraphQL
import config.getAll
import config.modules
import config.setup
import graphql.usecases
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.*
import kotlinx.coroutines.launch

fun main(args: Array<String>) = EngineMain.main(args)

internal const val AUTH_COOKIE = "JWT"

@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        gson()
    }

    val config = config()

    install(KoinPlugin) {
        modules(modules(config))
    }

    loginModule(config)

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

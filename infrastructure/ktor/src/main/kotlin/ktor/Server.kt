package ktor

import config.getAll
import config.modules
import config.setup
import graphql.usecases
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import kotlinx.coroutines.launch
import ktor.plugins.GraphQL
import ktor.plugins.get
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    val config = config()

    install(Koin) {
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
                +it.user
            }
        }

        schema(usecases(getAll()))
    }

    launch {
        setup(get(), get())
    }
}

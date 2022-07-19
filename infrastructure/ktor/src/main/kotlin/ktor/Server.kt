package ktor

import config.getAll
import config.modules
import config.setup
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.defaultheaders.*
import kotlinx.coroutines.launch
import ktor.plugins.get
import ktor.plugins.graphql.GraphQL
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
        wrap {
            authenticate(optional = true, build = it)
        }

        functions = getAll()
    }

    launch {
        setup(get(), get())
    }
}

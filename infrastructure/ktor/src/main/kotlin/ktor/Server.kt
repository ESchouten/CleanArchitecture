package ktor

import config.getAll
import config.modules
import config.setup
import graphql.usecases
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.component.Components
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.security.BearerAuth
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.defaultheaders.*
import kotlinx.coroutines.launch
import ktor.plugins.GraphQL
import ktor.plugins.get
import ktor.plugins.rest
import org.koin.ktor.plugin.Koin
import usecases.usecase.UsecaseType
import java.time.Instant
import java.util.*
import kotlin.reflect.typeOf

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

    val usecases = getAll<UsecaseType<*>>()

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

        schema(usecases(usecases))
    }

    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info(
                title = "CleanArchitecture",
                version = "1.0.0"
            ),
            components = Components(
                securitySchemes = mutableMapOf(
                    "bearer" to BearerAuth()
                )
            )
        )
        customTypes = mapOf(
            typeOf<Instant>() to TypeDefinition(type = "string", format = "date-time"),
            typeOf<Date>() to TypeDefinition(type = "string", format = "date-time")
        )
    }

    rest(usecases)

    launch {
        setup(get(), get())
    }
}

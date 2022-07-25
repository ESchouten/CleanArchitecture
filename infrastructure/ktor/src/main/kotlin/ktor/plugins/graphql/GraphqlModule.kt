package ktor.plugins.graphql

import com.expediagroup.graphql.generator.extensions.print
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.buildSchema
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import usecases.usecase.UsecaseType

/**
 * Graphql Feature - refactor of KGraphql module
 * Configuration - functions -> contains all functions from usecases (queries and mutations)
 */
class GraphQL() {

    class Configuration {
        /**
         * This adds support for opening the graphql route within the browser
         */
        var playground: Boolean = true

        var endpoint: String = "/graphql"

        fun wrap(block: Route.(next: Route.() -> Unit) -> Unit) {
            wrapWith = block
        }

        internal var wrapWith: (Route.(next: Route.() -> Unit) -> Unit)? = null

        lateinit var functions: Collection<UsecaseType<*>>
    }


    companion object Feature : BaseApplicationPlugin<Application, Configuration, GraphQL> {
        override val key = AttributeKey<GraphQL>("GraphQL")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): GraphQL {
            val config = Configuration().apply(configure)
            val mapper = jacksonObjectMapper()
            val schema = buildSchema(config.functions.toList())
            val ktorGraphQLServer = getGraphQLServer(mapper, schema)

            val routing: Routing.() -> Unit = {
                val routing: Route.() -> Unit = {
                    route(config.endpoint) {
                        post {
                            // Execute the query against the schema
                            val result = ktorGraphQLServer.execute(call.request)

                            if (result != null) {
                                // write response as json
                                val json = mapper.writeValueAsString(result)
                                call.respondText(json, contentType = ContentType.Application.Json)
                                //call.response.call.respond(json)
                            } else {
                                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                            }
                        }
                        if (config.playground) get {
                            call.respondText(buildPlaygroundHtml("graphql", "subscriptions"), ContentType.Text.Html)
                        }
                    }
                    get("sdl") { call.respondText(schema.print()) }
                }

                config.wrapWith?.invoke(this, routing) ?: routing(this)
            }

            pipeline.pluginOrNull(Routing)?.apply(routing) ?: pipeline.install(Routing, routing)

            return GraphQL()
        }

        private fun buildPlaygroundHtml(graphQLEndpoint: String, subscriptionsEndpoint: String) =
            Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
                ?.replace("\${graphQLEndpoint}", graphQLEndpoint)
                ?.replace("\${subscriptionsEndpoint}", subscriptionsEndpoint)
                ?: throw IllegalStateException("graphql-playground.html cannot be found in the classpath")
    }
}
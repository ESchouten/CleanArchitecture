package ktor.plugins.graphql

import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.execution.GraphQLServer
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.getGraphQLObject
import graphql.schema.GraphQLSchema
import io.ktor.server.request.*

/**
 * Base Graphql Server
 */
class KtorGraphQLServer(
    requestParser: KtorGraphQLRequestParser,
    contextFactory: KtorGraphQLContextFactory,
    requestHandler: GraphQLRequestHandler
) : GraphQLServer<ApplicationRequest>(requestParser, contextFactory, requestHandler)

fun getGraphQLServer(mapper: ObjectMapper, schema: GraphQLSchema): KtorGraphQLServer {
    val requestParser = KtorGraphQLRequestParser(mapper)
    val contextFactory = KtorGraphQLContextFactory()
    val graphQL = getGraphQLObject(schema)
    val requestHandler = GraphQLRequestHandler(graphQL)

    return KtorGraphQLServer(requestParser, contextFactory, requestHandler)
}
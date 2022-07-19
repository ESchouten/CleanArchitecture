package ktor.plugins.graphql

import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import io.ktor.server.auth.*
import io.ktor.server.request.*
import ktor.UserPrincipal

/**
 * Custom logic for context
 * Get authenticated user and add to contextMap
 */
class KtorGraphQLContextFactory : GraphQLContextFactory<GraphQLContext, ApplicationRequest> {

    override suspend fun generateContextMap(request: ApplicationRequest): Map<Any, Any> {
        val user = request.call.authentication.principal<UserPrincipal>()?.user

        user?.let { return mutableMapOf("userPrincipal" to user) }

        return mutableMapOf()
    }

    override suspend fun generateContext(request: ApplicationRequest): GraphQLContext? {
        return null
    }
}
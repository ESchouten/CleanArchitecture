package graphql

import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/**
 * Custom data fetcher to map authenticated user to authentication parameter in usecase
 */
class CustomDataFetcherFactory: SimpleKotlinDataFetcherFactoryProvider() {
    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) = DataFetcherFactory {
        CustomFunctionDataFetcher(
            target,
            kFunction
        )
    }
}

class CustomFunctionDataFetcher(target: Any?, fn: KFunction<*>) : FunctionDataFetcher(target, fn) {
    override fun mapParameterToValue(param: KParameter, environment: DataFetchingEnvironment): Pair<KParameter, Any?>? =
        when (param.name) {
            "authentication" -> param to environment.graphQlContext.get("userPrincipal")
            else -> super.mapParameterToValue(param, environment)
        }
}
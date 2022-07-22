package graphql

import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KClass
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

// map usecase class with map of original argument name - new name {usecase class - [old name * new name]}
val paramMap = mutableMapOf<KClass<*>, MutableMap<String, String>>()
class CustomFunctionDataFetcher(target: Any?, fn: KFunction<*>) : FunctionDataFetcher(target, fn) {
    // get function class name (there's a better way to get the class??)
    private val className = fn.toString().substringBeforeLast(".invoke").substringAfter(" ")
    private val kClass = Class.forName(className).kotlin
    // as the convertArgumentValue is an internal method we have to use reflections to get it
    private val convertArgumentValue = Class.forName("com.expediagroup.graphql.generator.execution.ConvertArgumentValueKt")
        .declaredMethods.find { it.name == "convertArgumentValue" }!!


    override fun mapParameterToValue(param: KParameter, environment: DataFetchingEnvironment): Pair<KParameter, Any?>? =
        when {
            param.name == "authentication" -> param to environment.graphQlContext.get("userPrincipal")
            paramMap[kClass]?.get(param.name) != null -> {
                // call the convertArgumentValue function
                param to convertArgumentValue(this, paramMap[kClass]!![param.name], param, environment.arguments)
            }
            else -> super.mapParameterToValue(param, environment)
        }
}
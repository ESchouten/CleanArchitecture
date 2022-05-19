package ktor.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

fun Routing.getKoin() = GlobalContext.get()
fun Application.getKoin(): Koin = GlobalContext.get()

inline fun <reified T : Any> Application.get(
    qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null
) = getKoin().get<T>(qualifier, parameters)

inline fun <reified T : Any> Routing.get(
    qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null
) = getKoin().get<T>(qualifier, parameters)
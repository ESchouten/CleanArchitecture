package ktor.plugins

import io.ktor.events.EventDefinition
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

val KApplicationStarted = EventDefinition<KoinApplication>()
val KApplicationStopPreparing = EventDefinition<KoinApplication>()
val KApplicationStopped = EventDefinition<KoinApplication>()

// Custom Koin plugin since Koin has no Ktor 2.0 support yet
internal class Koin(internal val koinApplication: KoinApplication) {
    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, KoinApplication, ktor.plugins.Koin> {
        override val key = AttributeKey<ktor.plugins.Koin>("CustomKoinPlugin")
        override fun install(
            pipeline: ApplicationCallPipeline, configure: KoinApplication.() -> Unit
        ): ktor.plugins.Koin {
            val monitor = pipeline.environment?.monitor
            val koinApplication = startKoin(appDeclaration = configure)
            if (monitor != null) {
                monitor.raise(KApplicationStarted, koinApplication)
                monitor.subscribe(ApplicationStopping) {
                    monitor.raise(KApplicationStopPreparing, koinApplication)
                    stopKoin()
                    monitor.raise(KApplicationStopped, koinApplication)
                }
            }
            return Koin(koinApplication)
        }
    }
}

fun Routing.getKoin() = GlobalContext.get()
fun Application.getKoin(): Koin = GlobalContext.get()

inline fun <reified T : Any> Application.get(
    qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null
) = getKoin().get<T>(qualifier, parameters)

inline fun <reified T : Any> Routing.get(
    qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null
) = getKoin().get<T>(qualifier, parameters)
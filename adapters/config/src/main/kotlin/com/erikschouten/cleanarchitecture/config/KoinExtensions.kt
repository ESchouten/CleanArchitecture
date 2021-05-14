package org.koin.experimental.builder

import org.koin.core.logger.Level
import org.koin.core.scope.Scope
import org.koin.core.time.measureDurationForResult
import org.koin.ext.getFullName
import kotlin.reflect.KClass

fun <T : Any> Scope.create(kClass: KClass<T>): T {
    val instance: Any

    if (logger.level == Level.DEBUG) {
        logger.debug("!- creating class:${kClass.getFullName()}")
    }

    val constructor = kClass.java.constructors.firstOrNull()
        ?: error("No constructor found for class '${kClass.getFullName()}'")

    val args = if (logger.level == Level.DEBUG) {
        val (_args, duration) = measureDurationForResult {
            getArguments(constructor, this)
        }
        logger.debug("!- got arguments in $duration ms")
        _args
    } else {
        getArguments(constructor, this)
    }

    instance = if (logger.level == Level.DEBUG) {
        val (_instance, duration) = measureDurationForResult {
            createInstance(args, constructor)
        }
        logger.debug("!- created instance in $duration ms")
        _instance
    } else {
        createInstance(args, constructor)
    }
    return instance as T
}

package logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggerImpl : usecases.dependency.Logger {
    private val loggers = mutableMapOf<String, Logger>()

    private val logger: Logger
        get() = Thread.currentThread().stackTrace[3].className.split(".").first().uppercase().let { module ->
            loggers.getOrPut(module) { LoggerFactory.getLogger(module) }
        }

    override fun debug(log: String) {
        logger.debug(log)
    }

    override fun info(log: String) {
        logger.info(log)
    }

    override fun warn(log: String) {
        logger.warn(log)
    }

    override fun error(log: String) {
        logger.error(log)
    }
}

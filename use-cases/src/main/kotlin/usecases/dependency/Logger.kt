package usecases.dependency

interface Logger {
    fun debug(log: String)
    fun info(log: String)
    fun warn(log: String)
    fun error(log: String)
}
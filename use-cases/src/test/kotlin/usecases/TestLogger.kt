package usecases

import usecases.dependency.Logger

val logger = TestLogger()

class TestLogger : Logger {
    override fun debug(log: String) {
        println(log)
    }

    override fun info(log: String) {
        println(log)
    }

    override fun warn(log: String) {
        println(log)
    }

    override fun error(log: String) {
        println(log)
    }
}
package usecases

import usecases.usecase.UsecaseType

interface UsecaseTests {
    val usecase: UsecaseType<*>

    fun success()
    fun unauthenticated()
    fun `No user roles`()
}

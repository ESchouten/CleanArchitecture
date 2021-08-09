package usecases.usecase

interface UsecaseTests {
    val usecase: UsecaseType<*>

    fun success()
    fun unauthenticated()
    fun `No user roles`()
}

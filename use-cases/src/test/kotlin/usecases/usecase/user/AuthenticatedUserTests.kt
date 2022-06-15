package usecases.usecase.user

import domain.LoginException
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticatedUserTests : UsecaseTests {

    override val usecase = AuthenticatedUser(logger)

    @Test
    override fun success() {
        runBlocking {
            val result = usecase(userModel)
            assertEquals(result, userModel)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null)
            }
        }
    }

    @Test
    override fun `No user roles`() = success()
}

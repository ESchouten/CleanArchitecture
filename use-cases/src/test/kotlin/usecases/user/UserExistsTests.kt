package usecases.user

import domain.AuthorizationException
import domain.LoginException
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.UsecaseTests
import usecases.usecase.user.UserExists
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserExistsTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = UserExists(repository)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns user
            val result = usecase(userModel, email)
            assertEquals(result, true)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, email)
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), email)
            }
        }
    }

    @Test
    fun `User does not exist`() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns null
            val result = usecase(userModel, email)
            assertEquals(result, false)
        }
    }
}

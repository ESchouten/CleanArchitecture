package usecases.usecase.user

import domain.AuthorizationException
import domain.LoginException
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ListUsersTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = ListUsers(repository)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findAll() } } returns listOf(user)
            val result = usecase(userModel)
            assertEquals(result, listOf(userModel))
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
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()))
            }
        }
    }
}

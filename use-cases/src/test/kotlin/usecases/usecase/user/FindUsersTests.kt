package usecases.usecase.user

import domain.AuthorizationException
import domain.LoginException
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FindUsersTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = FindUsers(repository)

    @Test
    override fun success() {
        runBlocking {
            val emails = listOf(user.email)
            every { runBlocking { repository.findAllByEmails(emails) } } returns listOf(user)
            val result = usecase(userModel, emails)
            assertEquals(result, listOf(userModel))
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, listOf(user.email))
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), listOf(user.email))
            }
        }
    }
}

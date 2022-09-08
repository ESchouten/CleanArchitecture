package usecases.usecase.user

import domain.AuthorizationException
import domain.LoginException
import domain.repository.Pagination
import domain.repository.PaginationResult
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUsersTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = GetUsers(logger, repository)
    val pagination = Pagination(10, 0)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findAll(any()) } } returns PaginationResult(listOf(user), 1)
            val result = usecase(userModel, pagination).items
            assertEquals(result, listOf(userModel))
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, pagination)
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), pagination)
            }
        }
    }
}

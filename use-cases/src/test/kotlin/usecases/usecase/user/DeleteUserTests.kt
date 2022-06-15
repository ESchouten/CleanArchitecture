package usecases.usecase.user

import domain.AuthorizationException
import domain.LoginException
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.model.UserModel
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DeleteUserTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = DeleteUser(logger, repository)

    val userModel = UserModel(user)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.delete(any()) } } returns Unit
            usecase(userModel, id)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, id)
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), id)
            }
        }
    }
}

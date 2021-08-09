package usecases.usecase.user

import domain.AuthorizationException
import domain.EmailAlreadyExistsException
import domain.LoginException
import domain.entity.user.Email
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.model.UpdateUserModel
import usecases.model.UserModel
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateUserTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    val userExists = UserExists(repository)
    override val usecase = UpdateUser(repository, userExists)

    val updateUserModel = UpdateUserModel(user.id, user.email, user.authorities)
    val userModel = UserModel(updateUserModel.id, updateUserModel.email, updateUserModel.authorities)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findById(updateUserModel.id) } } returns user
            every { runBlocking { repository.update(any()) } } returns user
            val result = usecase(userModel, updateUserModel)

            assertEquals(userModel, result)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, updateUserModel)
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), updateUserModel)
            }
        }
    }

    @Test
    fun `User already exists`() {
        runBlocking {
            assertFailsWith<EmailAlreadyExistsException> {
                val newEmail = Email("calvin@cargoledger.nl")
                every { runBlocking { repository.findById(updateUserModel.id) } } returns user
                every { runBlocking { repository.findByEmail(newEmail) } } returns user.copy(email = newEmail)
                every { runBlocking { repository.update(any()) } } returns user
                val result = usecase(userModel, updateUserModel.copy(email = newEmail))

                assertEquals(result, userModel.copy(email = newEmail))
            }
        }
    }
}

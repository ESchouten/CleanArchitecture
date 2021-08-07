package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.EmailAlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.UsecaseTests
import com.erikschouten.cleanarchitecture.usecases.model.UpdateUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UpdateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `No User role`() {
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

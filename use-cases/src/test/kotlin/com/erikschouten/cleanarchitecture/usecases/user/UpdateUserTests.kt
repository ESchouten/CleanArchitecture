package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.*
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.Password
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UpdateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import com.erikschouten.cleanarchitecture.usecases.value
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateUserTests {

    val repository = mockk<UserRepository>()
    val userExists = UserExists(repository)
    val usecase = UpdateUser(repository, userExists)

    val userModel = UserModel(user.id, user.email, user.authorities)

    @Test
    fun `Successful update`() {
        runBlocking {
            every { runBlocking { repository.findById(userModel.id) } } returns user
            every { runBlocking { repository.update(any()) } } returns user
            val result = usecase(userModel, userModel)

            assertEquals(result, userModel)
        }
    }

    @Test
    fun Unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, userModel)
            }
        }
    }

    @Test
    fun `No User role`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), userModel)
            }
        }
    }

    @Test
    fun `User already exists`() {
        runBlocking {
            assertFailsWith<EmailAlreadyExistsException> {
                val newEmail = Email("calvin@cargoledger.nl")
                every { runBlocking { repository.findById(userModel.id) } } returns user
                every { runBlocking { repository.findByEmail(newEmail) } } returns user.copy(email = newEmail)
                every { runBlocking { repository.update(any()) } } returns user
                val result = usecase(userModel, userModel.copy(email = newEmail))

                assertEquals(result, userModel)
            }
        }
    }
}

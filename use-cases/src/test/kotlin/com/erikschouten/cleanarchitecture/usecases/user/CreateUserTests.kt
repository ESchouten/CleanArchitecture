package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.*
import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import com.erikschouten.cleanarchitecture.usecases.value
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userExists = UserExists(repository)
    val createUser = CreateUser(repository, userExists, passwordEncoder)

    val createUserModel = CreateUserModel(email, listOf(Authorities.USER), password)

    @Test
    fun `Successful creation`() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns null
            every { runBlocking { repository.create(any()) } } returns user
            every { passwordEncoder.encode(password) } returns value(PasswordHash(password.value.reversed()))
            val result = createUser(userModel, createUserModel)

            assertEquals(result, userModel)
        }
    }

    @Test
    fun Unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                createUser(null, createUserModel)
            }
        }
    }

    @Test
    fun `No User role`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                createUser(userModel.copy(authorities = emptyList()), createUserModel)
            }
        }
    }

    @Test
    fun `Invalid email`() {
        runBlocking {
            assertFailsWith<EmailInvalidException> {
                every { runBlocking { repository.findByEmail(Email("erik")) } } returns null
                createUser(userModel, createUserModel.copy(email = Email("erik")))
            }
        }
    }

    @Test
    fun `Invalid password`() {
        runBlocking {
            assertFailsWith<PasswordInvalidException> {
                every { runBlocking { repository.findByEmail(email) } } returns null
                every { passwordEncoder.encode(Password("pass")) } returns PasswordHash("pass".reversed())
                createUser(userModel, createUserModel.copy(password = Password("pass")))
            }
        }
    }

    @Test
    fun `User already exists`() {
        runBlocking {
            assertFailsWith<EmailAlreadyExistsException> {
                every { runBlocking { repository.findByEmail(email) } } returns user
                createUser(userModel, createUserModel)
            }
        }
    }
}

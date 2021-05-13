package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.*
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.entity.Authorities
import com.erikschouten.cleanarchitecture.entity.Email
import com.erikschouten.cleanarchitecture.entity.Password
import com.erikschouten.cleanarchitecture.entity.PasswordHash
import com.erikschouten.cleanarchitecture.model.CreateUserModel
import com.erikschouten.cleanarchitecture.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userExists = UserExists(repository)
    val createUser = CreateUser(repository, userExists, passwordEncoder)

    val createUserModel = CreateUserModel(email.value, listOf(Authorities.USER), password.value)

    @Test
    fun `Successful creation`() {
        every { repository.findByEmail(email) } returns null
        every { repository.save(any()) } returns user
        every { passwordEncoder.encode(password) } returns value(PasswordHash(password.value.reversed()))
        val result = createUser(userModel, createUserModel)

        assertEquals(result, userModel)
    }

    @Test
    fun Unauthenticated() {
        assertFailsWith<LoginException> {
            createUser(null, createUserModel)
        }
    }

    @Test
    fun `No User role`() {
        assertFailsWith<AuthorizationException> {
            createUser(userModel.copy(authorities = emptyList()), createUserModel)
        }
    }

    @Test
    fun `Invalid email`() {
        assertFailsWith<EmailInvalidException> {
            every { repository.findByEmail(Email("erik")) } returns null
            createUser(userModel, createUserModel.copy(email = "erik"))
        }
    }

    @Test
    fun `Invalid password`() {
        assertFailsWith<PasswordInvalidException> {
            every { repository.findByEmail(email) } returns null
            every { passwordEncoder.encode(Password("pass")) } returns PasswordHash("pass".reversed())
            createUser(userModel, createUserModel.copy(password = "pass"))
        }
    }

    @Test
    fun `User already exists`() {
        assertFailsWith<EmailAlreadyExistsException> {
            every { repository.findByEmail(email) } returns user
            createUser(userModel, createUserModel)
        }
    }
}

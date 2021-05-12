package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.*
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.entity.Authorities
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

    val createUserModel = CreateUserModel(email, listOf(Authorities.USER), password)

    @Test
    fun `Successful creation`() {
        every { repository.findByEmail(email) } returns null
        every { repository.save(any()) } returns user
        every { passwordEncoder.encode(password) } returns password.reversed()
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
            every { repository.findByEmail("erik") } returns null
            every { passwordEncoder.encode(password) } returns password.reversed()
            createUser(userModel, createUserModel.copy(email = "erik"))
        }
    }

    @Test
    fun `Invalid password`() {
        assertFailsWith<PasswordInvalidException> {
            every { repository.findByEmail(email) } returns null
            every { passwordEncoder.encode("pass") } returns "pass".reversed()
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

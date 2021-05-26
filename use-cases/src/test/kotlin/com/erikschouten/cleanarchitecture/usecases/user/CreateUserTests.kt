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
        every { repository.create(any()) } returns user
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

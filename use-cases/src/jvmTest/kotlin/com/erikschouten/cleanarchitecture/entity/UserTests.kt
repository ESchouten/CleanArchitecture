package com.erikschouten.cleanarchitecture.entity

import com.erikschouten.cleanarchitecture.EmailInvalidException
import com.erikschouten.cleanarchitecture.PasswordInvalidException
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecase.user.email
import com.erikschouten.cleanarchitecture.usecase.user.password
import com.erikschouten.cleanarchitecture.usecase.user.passwordHash
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UserTests {

    val passwordEncoder = mockk<PasswordEncoder>()

    @Test
    fun Valid() {
        User(email, listOf(Authorities.USER), passwordHash)
    }

    @Test
    fun `Invalid Email`() {
        every { passwordEncoder.encode(password) } returns PasswordHash(password.value.reversed())
        assertFailsWith<EmailInvalidException> {
            Email("erik")
        }
        assertFailsWith<EmailInvalidException> {
            CreateUserModel("erik", listOf(Authorities.USER), password.value).toUser(passwordEncoder)
        }
    }

    @Test
    fun `Invalid Password`() {
        every { passwordEncoder.encode(password) } returns PasswordHash(password.value.reversed())
        assertFailsWith<PasswordInvalidException> {
            Password("pass")
        }
        assertFailsWith<PasswordInvalidException> {
            CreateUserModel(email.value, listOf(Authorities.USER), "pass").toUser(passwordEncoder)
        }
    }
}

package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.LoginUserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.LoginUser
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginUserTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val authenticator = mockk<Authenticator>()
    val usecase = LoginUser(repository, authenticator, passwordEncoder)

    val loginUserModel = LoginUserModel(email.value, password.value)

    @Test
    fun `Successful login`() {
        every { repository.findByEmail(email) } returns user
        every { passwordEncoder.matches(password, PasswordHash(password.value.reversed())) } returns true
        every { authenticator.generate(user.id) } returns "token"
        val result = usecase(null, loginUserModel)
        assertEquals(result, "token")
    }

    @Test
    fun `Invalid email`() {
        assertFailsWith<LoginException> {
            every { repository.findByEmail(email) } returns null
            usecase(null, loginUserModel)
        }
    }

    @Test
    fun `Invalid password`() {
        assertFailsWith<LoginException> {
            val pass = password.value + 1
            every { repository.findByEmail(email) } returns user
            every { passwordEncoder.matches(Password(pass), PasswordHash(password.value.reversed())) } returns false
            usecase(null, loginUserModel.copy(password = pass))
        }
    }
}

package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.LoginException
import com.erikschouten.cleanarchitecture.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserExistsTests {

    val repository = mockk<UserRepository>()
    val usecase = UserExists(repository)

    @Test
    fun `User exists`() {
        every { repository.findByEmail(email) } returns user
        val result = usecase(userModel, email)
        assertEquals(result, true)
    }

    @Test
    fun Unauthenticated() {
        assertFailsWith<LoginException> {
            usecase(null, email)
        }
    }

    @Test
    fun `User does not exist`() {
        every { repository.findByEmail(email) } returns null
        val result = usecase(userModel, email)
        assertEquals(result, false)
    }
}

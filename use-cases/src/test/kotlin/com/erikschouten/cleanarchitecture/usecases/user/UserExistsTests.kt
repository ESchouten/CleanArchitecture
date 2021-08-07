package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.UsecaseTests
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserExistsTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = UserExists(repository)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns user
            val result = usecase(userModel, email)
            assertEquals(result, true)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, email)
            }
        }
    }

    @Test
    fun `User does not exist`() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns null
            val result = usecase(userModel, email)
            assertEquals(result, false)
        }
    }
}

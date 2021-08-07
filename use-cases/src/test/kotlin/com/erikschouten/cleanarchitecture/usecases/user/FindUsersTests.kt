package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.UsecaseTests
import com.erikschouten.cleanarchitecture.usecases.usecase.user.FindUsers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FindUsersTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = FindUsers(repository)

    @Test
    override fun success() {
        runBlocking {
            val emails = listOf(user.email)
            every { runBlocking { repository.findAllByEmails(emails) } } returns listOf(user)
            val result = usecase(userModel, emails)
            assertEquals(result, listOf(userModel))
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, listOf(user.email))
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), listOf(user.email))
            }
        }
    }
}

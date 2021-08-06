package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.usecase.user.FindUsers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FindUsersTests {

    val repository = mockk<UserRepository>()
    val usecase = FindUsers(repository)

    @Test
    fun `Authenticated, should return User list`() {
        runBlocking {
            val emails = listOf(user.email)
            every { runBlocking { repository.findAllByEmails(emails) } } returns listOf(user)
            val result = usecase(userModel, emails)
            assertEquals(result, listOf(userModel))
        }
    }

    @Test
    fun Unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, listOf(user.email))
            }
        }
    }

    @Test
    fun `No User role`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), listOf(user.email))
            }
        }
    }
}

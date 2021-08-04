package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.usecase.user.ListUsers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ListUsersTests {

    val repository = mockk<UserRepository>()
    val usecase = ListUsers(repository)

    @Test
    fun `Authenticated, should return UserModelArray`() {
        runBlocking {
            every { runBlocking { repository.findAll() } } returns listOf(user)
            val result = usecase(userModel)
            assertEquals(result, listOf(userModel))
        }
    }

    @Test
    fun Unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null)
            }
        }
    }

    @Test
    fun `No User role`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()))
            }
        }
    }
}

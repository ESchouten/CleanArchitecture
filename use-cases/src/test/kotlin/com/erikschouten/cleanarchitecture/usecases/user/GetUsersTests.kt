package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.UsecaseTests
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.GetUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserTest : UsecaseTests {

    val repository = mockk<UserRepository>()
    override val usecase = GetUser(repository)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findById(id) } } returns user
            val result = usecase(userModel, id)
            assertEquals(result, UserModel(user))
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, id)
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), id)
            }
        }
    }
}

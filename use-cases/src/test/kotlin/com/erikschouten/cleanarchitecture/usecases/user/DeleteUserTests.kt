package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.DeleteUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DeleteUserTests {

    val repository = mockk<UserRepository>()
    val usecase = DeleteUser(repository)

    val userModel = UserModel(user.id, user.email, user.authorities)

    @Test
    fun `Successful deletion`() {
        runBlocking {
            every { runBlocking { repository.delete(any()) } } returns Unit
            usecase(userModel, -1)
        }
    }

    @Test
    fun Unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, -1)
            }
        }
    }

    @Test
    fun `No User role`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), -1)
            }
        }
    }
}

package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.*
import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.Password
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.CreateUserModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.CreateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.DeleteUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UpdateUser
import com.erikschouten.cleanarchitecture.usecases.usecase.user.UserExists
import com.erikschouten.cleanarchitecture.usecases.value
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteUserTests {

    val repository = mockk<UserRepository>()
    val usecase = DeleteUser(repository)

    val userModel = UserModel(user.id, user.email, user.authorities)

    @Test
    fun `Successful deletion`() {
        runBlocking {
            every { runBlocking { repository.delete(any()) } } returns Unit
            usecase(userModel, UUID.randomUUID())
        }
    }

    @Test
    fun Unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, UUID.randomUUID())
            }
        }
    }

    @Test
    fun `No User role`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), UUID.randomUUID())
            }
        }
    }
}

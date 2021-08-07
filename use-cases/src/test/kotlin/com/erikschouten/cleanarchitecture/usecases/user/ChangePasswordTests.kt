package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.AuthorizationException
import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.domain.UserNotFoundException
import com.erikschouten.cleanarchitecture.domain.entity.user.Password
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.usecases.UsecaseTests
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.usecases.model.ChangeOwnPasswordModel
import com.erikschouten.cleanarchitecture.usecases.model.ChangePasswordModel
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import com.erikschouten.cleanarchitecture.usecases.usecase.user.ChangeOwnPassword
import com.erikschouten.cleanarchitecture.usecases.usecase.user.ChangePassword
import com.erikschouten.cleanarchitecture.usecases.value
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ChangePasswordTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    override val usecase = ChangePassword(repository, passwordEncoder)
    val changePasswordMock = mockk<ChangePassword>()
    val changeOwnPassword = ChangeOwnPassword(repository, changePasswordMock, passwordEncoder)

    val newPassword = Password(password.value + 1)
    val newPasswordHash = PasswordHash(newPassword.value.reversed())
    val changePasswordModel = ChangePasswordModel(user.id, newPassword)
    val changeOwnPasswordModel = ChangeOwnPasswordModel(password, newPassword)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findById(userModel.id) } } returns user
            every { passwordEncoder.encode(newPassword) } returns value(PasswordHash(newPassword.value.reversed()))
            every { runBlocking { repository.update(any()) } } returns user.copy(password = newPasswordHash)

            usecase(userModel, changePasswordModel)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, changePasswordModel)
            }
        }
    }

    @Test
    fun `No User role, own user`() {
        runBlocking {
            every { runBlocking { repository.findById(userModel.id) } } returns user
            every { passwordEncoder.encode(newPassword) } returns value(PasswordHash(newPassword.value.reversed()))
            every { runBlocking { repository.update(any()) } } returns user.copy(password = newPasswordHash)

            usecase(userModel.copy(authorities = emptyList()), changePasswordModel)
        }
    }

    @Test
    fun `Change own password`() {
        runBlocking {
            every { runBlocking { repository.findById(userModel.id) } } returns user
            every { passwordEncoder.matches(password, PasswordHash(password.value.reversed())) } returns true
            every {
                runBlocking {
                    changePasswordMock.invoke(
                        any(),
                        any()
                    )
                }
            } returns UserModel(user.copy(password = newPasswordHash))

            changeOwnPassword(userModel, changeOwnPasswordModel)
        }
    }

    @Test
    fun `Change own password, wrong current password`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                every { runBlocking { repository.findById(userModel.id) } } returns user
                every { passwordEncoder.matches(newPassword, PasswordHash(password.value.reversed())) } returns false

                changeOwnPassword(userModel, changeOwnPasswordModel.copy(current = newPassword))
            }
        }
    }

    @Test
    fun `No User role, other user`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(
                    userModel.copy(authorities = emptyList()),
                    changePasswordModel.copy(id = 1)
                )
            }
        }
    }

    @Test
    fun `User not found`() {
        runBlocking {
            assertFailsWith<UserNotFoundException> {
                val id = -1
                every { runBlocking { repository.findById(id) } } returns null
                usecase(userModel, changePasswordModel.copy(id = id))
            }
        }
    }
}

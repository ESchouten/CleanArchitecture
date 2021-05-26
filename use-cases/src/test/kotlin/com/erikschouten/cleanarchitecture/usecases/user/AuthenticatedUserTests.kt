package com.erikschouten.cleanarchitecture.usecases.user

import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.usecases.usecase.user.AuthenticatedUser
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticatedUserTests {

    val usecase = AuthenticatedUser()

    @Test
    fun `Authenticated, should return UserModel`() {
        runBlocking {
            val result = usecase(userModel)
            assertEquals(result, userModel)
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
}

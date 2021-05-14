package com.erikschouten.cleanarchitecture.usecase.user

import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.usecases.usecase.user.AuthenticatedUser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticatedUserTests {

    val usecase = AuthenticatedUser()

    @Test
    fun `Authenticated, should return UserModel`() {
        val result = usecase(userModel)
        assertEquals(result, userModel)
    }

    @Test
    fun Unauthenticated() {
        assertFailsWith<LoginException> {
            usecase(null)
        }
    }
}

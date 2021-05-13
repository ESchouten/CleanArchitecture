package com.erikschouten.cleanarchitecture.entity

import com.erikschouten.cleanarchitecture.EmailInvalidException
import com.erikschouten.cleanarchitecture.PasswordInvalidException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UserTests {

    @Test
    fun Valid() {
        User(Email("erik@erikschouten.com"), listOf(Authorities.USER), PasswordHash("P@ssw0rd!".reversed()))
    }

    @Test
    fun `Invalid Email`() {
        assertFailsWith<EmailInvalidException> {
            Email("erik")
        }
    }

    @Test
    fun `Invalid Password`() {
        assertFailsWith<PasswordInvalidException> {
            Password("pass")
        }
    }
}

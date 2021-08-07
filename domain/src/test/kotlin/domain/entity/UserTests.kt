package domain.entity

import domain.EmailInvalidException
import domain.PasswordInvalidException
import domain.entity.user.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UserTests {

    @Test
    fun Valid() {
        User(
            email = Email("erik@erikschouten.com"),
            authorities = listOf(Authorities.USER),
            password = PasswordHash("P@ssw0rd!".reversed())
        )
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

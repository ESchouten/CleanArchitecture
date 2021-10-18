package domain.entity

import domain.EmailInvalidException
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UserTests {

    @Test
    fun `Valid`() {
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
}

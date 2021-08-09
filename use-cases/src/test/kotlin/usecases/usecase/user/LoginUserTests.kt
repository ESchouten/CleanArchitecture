package usecases.usecase.user

import domain.LoginException
import domain.entity.user.Password
import domain.entity.user.PasswordHash
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.UsecaseTests
import usecases.dependency.Authenticator
import usecases.dependency.PasswordEncoder
import usecases.model.LoginUserModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginUserTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val authenticator = mockk<Authenticator>()
    override val usecase = LoginUser(repository, authenticator, passwordEncoder)

    val loginUserModel = LoginUserModel(email, password)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns user
            every { passwordEncoder.matches(password, PasswordHash(password.value.reversed())) } returns true
            every { authenticator.generate(user.id) } returns "token"
            val result = usecase(null, loginUserModel)
            assertEquals(result, "token")
        }
    }

    @Test
    override fun unauthenticated() = success()

    @Test
    override fun `No user roles`() = success()

    @Test
    fun `Invalid email`() {
        runBlocking {
            assertFailsWith<LoginException> {
                every { runBlocking { repository.findByEmail(email) } } returns null
                usecase(null, loginUserModel)
            }
        }
    }

    @Test
    fun `Invalid password`() {
        runBlocking {
            assertFailsWith<LoginException> {
                val pass = Password(password.value + 1)
                every { runBlocking { repository.findByEmail(email) } } returns user
                every { passwordEncoder.matches(pass, PasswordHash(password.value.reversed())) } returns false
                usecase(null, loginUserModel.copy(password = pass))
            }
        }
    }
}

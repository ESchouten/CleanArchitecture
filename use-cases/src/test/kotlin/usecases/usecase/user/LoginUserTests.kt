package usecases.usecase.user

import domain.LoginException
import domain.entity.Password
import domain.entity.PasswordHash
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.dependency.Authenticator
import usecases.dependency.PasswordEncoder
import usecases.logger
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginUserTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val authenticator = mockk<Authenticator>()
    override val usecase = LoginUser(logger, repository, authenticator, passwordEncoder)

    val loginUserModel = LoginUserModel(email, password)

    @Test
    override fun success() {
        runBlocking {
            val user = user.copy(authorities = emptyList(), locked = false)
            every { runBlocking { repository.findByEmail(email) } } returns user
            every { passwordEncoder.matches(password, PasswordHash(password.value.reversed())) } returns true
            every { authenticator.generate(UserModel(user)) } returns "token"
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

    @Test
    fun locked() {
        runBlocking {
            assertFailsWith<LoginException> {
                val user = user.copy(locked = true)
                every { runBlocking { repository.findByEmail(email) } } returns user
                usecase(null, loginUserModel)
            }
        }
    }
}

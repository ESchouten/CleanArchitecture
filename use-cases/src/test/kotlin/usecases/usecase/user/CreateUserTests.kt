package usecases.usecase.user

import domain.*
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.Password
import domain.entity.user.PasswordHash
import domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.UsecaseTests
import usecases.dependency.PasswordEncoder
import usecases.model.CreateUserModel
import usecases.value
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserTests : UsecaseTests {

    val repository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userExists = UserExists(repository)
    override val usecase = CreateUser(repository, userExists, passwordEncoder)

    val createUserModel = CreateUserModel(email, listOf(Authorities.USER), password)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repository.findByEmail(email) } } returns null
            every { runBlocking { repository.create(any()) } } returns user
            every { passwordEncoder.encode(password) } returns value(PasswordHash(password.value.reversed()))
            val result = usecase(userModel, createUserModel)

            assertEquals(result, userModel)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, createUserModel)
            }
        }
    }

    @Test
    override fun `No user roles`() {
        runBlocking {
            assertFailsWith<AuthorizationException> {
                usecase(userModel.copy(authorities = emptyList()), createUserModel)
            }
        }
    }

    @Test
    fun `Invalid email`() {
        runBlocking {
            assertFailsWith<EmailInvalidException> {
                every { runBlocking { repository.findByEmail(Email("erik")) } } returns null
                usecase(userModel, createUserModel.copy(email = Email("erik")))
            }
        }
    }

    @Test
    fun `Invalid password`() {
        runBlocking {
            assertFailsWith<PasswordInvalidException> {
                every { runBlocking { repository.findByEmail(email) } } returns null
                every { passwordEncoder.encode(Password("pass")) } returns PasswordHash("pass".reversed())
                usecase(userModel, createUserModel.copy(password = Password("pass")))
            }
        }
    }

    @Test
    fun `User already exists`() {
        runBlocking {
            assertFailsWith<EmailAlreadyExistsException> {
                every { runBlocking { repository.findByEmail(email) } } returns user
                usecase(userModel, createUserModel)
            }
        }
    }
}

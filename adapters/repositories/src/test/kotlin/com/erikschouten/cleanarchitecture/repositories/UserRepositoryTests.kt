package com.erikschouten.cleanarchitecture.repositories

import com.erikschouten.cleanarchitecture.domain.entity.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class UserRepositoryTests {

    private val email = Email("erik@erikschouten.com")
    private val password = Password("P@ssw0rd!")
    private val passwordHash = PasswordHash(password.value.reversed())
    private val user = User(email = email, authorities = listOf(Authorities.USER), password = passwordHash)

    @BeforeTest
    fun setup() {
        DatabaseFactory.init("org.h2.Driver", "jdbc:h2:mem:", "test")
    }

    @Test
    fun `User tests`() {
        runBlocking {
            val repository = ExposedUserRepository()
            // User does not exist
            assertNull(repository.findByEmail(email))
            // No users exist
            assertEquals(0, repository.findAll().size)
            //Create user
            val created = repository.create(user)
            assertNotNull(created)
            //Database should generate own UUID
            assertNotEquals(user.id, created.id)
            //Rest should be equal
            assertEquals(user.email, created.email)
            assertEquals(user.authorities, created.authorities)
            assertEquals(user.password, created.password)
            //Get created user
            assertEquals(created, repository.findByEmail(email))
            //Get all created users
            assertEquals(1, repository.findAll().size)
            assertEquals(created, repository.findAll().first())
        }
    }
}

package repositories

import domain.entity.user.*
import domain.repository.Pagination
import kotlinx.coroutines.runBlocking
import repositories.user.UserRepositoryImpl
import kotlin.test.*

class UserRepositoryTests {

    private val email = Email("erik@erikschouten.com")
    private val password = Password("P@ssw0rd!")
    private val passwordHash = PasswordHash(password.value.reversed())
    private val user = User(email = email, authorities = listOf(Authorities.USER), password = passwordHash)

    private val newEmail = Email("calvin@cargoledger.nl")
    private val newPassword = Password("P@ssw0rd!123")
    private val newPasswordHash = PasswordHash(newPassword.value.reversed())

    @BeforeTest
    fun setup() {
        DatabaseFactory.init("org.h2.Driver", "jdbc:h2:mem:", "test")
    }

    @Test
    fun `User tests`() {
        runBlocking {
            val repository = UserRepositoryImpl()
            // User does not exist
            assertNull(repository.findByEmail(email))
            // No users exist
            assertEquals(0, repository.findAll().size)
            // Create user
            val id = repository.create(user).id
            val created = repository.findById(id)
            assertNotNull(created)
            // Database should generate own Int
            assertNotEquals(user.id, created.id)
            // Rest should be equal
            assertEquals(user.email, created.email)
            assertEquals(user.authorities, created.authorities)
            assertEquals(user.password, created.password)
            // Get created user
            assertEquals(created, repository.findByEmail(email))
            assertEquals(created, repository.findAll(Pagination(10, 0, user.email.value)).items.first())
            // Get all created users
            assertEquals(1, repository.count())
            assertEquals(created, repository.findAll().first())
            // Update user data
            val update = created.copy(email = newEmail, authorities = listOf(), password = newPasswordHash)
            repository.update(update)
            val updated = repository.findById(id)
            assertNotNull(updated)
            //Match variables
            assertEquals(update.email, updated.email)
            assertContentEquals(update.authorities, updated.authorities)
            assertEquals(update.password, updated.password)
            assertEquals(1, repository.count())
            //Delete user
            repository.delete(id)
            assertEquals(0, repository.count())
        }
    }
}

package com.erikschouten.cleanarchitecture.repositories

import com.erikschouten.cleanarchitecture.domain.AlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ExposedUserRepository : UserRepository {
    override fun findByEmail(email: Email) = transaction {
        UserEntity.find { UserTable.email eq email.value }.firstOrNull()?.toUser()
    }
    override fun findById(id: UUID) = transaction {
        UserEntity.findById(id)?.toUser()
    }
    override fun findAll() = transaction {
        UserEntity.all().map { it.toUser() }
    }
    override fun create(entity: User) = transaction {
        UserEntity.new(entity).toUser()
    }
    override fun update(entity: User) = transaction {
        UserEntity[entity.id].apply {
            update(entity)
        }.toUser()
    }
}

internal object UserTable : UUIDTable() {
    val email = varchar("name", 50).index(isUnique = true)
    val authorities = text("authorities")
    val password = varchar("password", 200)
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var email by UserTable.email.transform({ email -> email.value}, { email -> Email(email) })
    var authorities by UserTable.authorities.transform(
        { authorities -> authorities.joinToString(SEPARATOR) },
        { authorities -> authorities.split(SEPARATOR).map { Authorities.valueOf(it) } }
    )
    var password by UserTable.password.transform({ password -> password.value}, { password -> PasswordHash(password) })

    companion object : UUIDEntityClass<UserEntity>(UserTable) {
        private const val SEPARATOR = ":"

        fun new(user: User) = UserEntity.new {
            email = user.email
            authorities = user.authorities
            password = user.password
        }
    }

    fun toUser() = User(id.value, email, authorities, password)
    fun update(user: User) {
        email = user.email
        authorities = user.authorities
        password = user.password
    }
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<UUID, User>()

    override fun findById(id: UUID) = users[id]
    override fun findAll() = users.values.toList()
    override fun create(entity: User): User {
        if (users.containsKey(entity.id)) throw AlreadyExistsException()
        return update(entity)
    }

    override fun update(entity: User): User {
        users[entity.id] = entity
        return entity
    }

    override fun findByEmail(email: Email) = users.values.find { it.email == email }
}

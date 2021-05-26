package com.erikschouten.cleanarchitecture.repositories

import com.erikschouten.cleanarchitecture.domain.AlreadyExistsException
import com.erikschouten.cleanarchitecture.domain.entity.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.Email
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash
import com.erikschouten.cleanarchitecture.domain.entity.User
import com.erikschouten.cleanarchitecture.domain.repository.UserRepository
import com.erikschouten.cleanarchitecture.repositories.DatabaseFactory.query
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

class ExposedUserRepository : UserRepository {
    override suspend fun findByEmail(email: Email) = query {
        UserEntity.find { UserTable.email eq email.value }.firstOrNull()?.toUser()
    }
    override suspend fun findById(id: UUID) = query {
        UserEntity.findById(id)?.toUser()
    }
    override suspend fun findAll() = query {
        UserEntity.all().map { it.toUser() }
    }
    override suspend fun create(entity: User) = query {
        UserEntity.new {
            email = entity.email
            authorities = entity.authorities
            password = entity.password
        }.toUser()
    }
    override suspend fun update(entity: User) = query {
        UserEntity[entity.id].apply {
            email = entity.email
            authorities = entity.authorities
            password = entity.password
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
    }

    fun toUser() = User(id.value, email, authorities, password)
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<UUID, User>()

    override suspend fun findById(id: UUID) = users[id]
    override suspend fun findAll() = users.values.toList()
    override suspend fun create(entity: User): User {
        if (users.containsKey(entity.id)) throw AlreadyExistsException()
        return update(entity)
    }

    override suspend fun update(entity: User): User {
        users[entity.id] = entity
        return entity
    }

    override suspend fun findByEmail(email: Email) = users.values.find { it.email == email }
}

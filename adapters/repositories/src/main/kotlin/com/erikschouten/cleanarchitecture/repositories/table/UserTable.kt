package com.erikschouten.cleanarchitecture.repositories.table

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.domain.entity.user.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

internal object UserTable : UUIDTable() {
    val email = varchar("name", 50).index(isUnique = true)
    val authorities = text("authorities")
    val password = varchar("password", 200)
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var email by UserTable.email.transform({ email -> email.value }, { email -> Email(email) })
    var authorities by UserTable.authorities.transform(
        { authorities -> authorities.joinToString(SEPARATOR) },
        { authorities -> authorities.split(SEPARATOR).map { Authorities.valueOf(it) } }
    )
    var password by UserTable.password.transform({ password -> password.value }, { password -> PasswordHash(password) })

    companion object : UUIDEntityClass<UserEntity>(UserTable) {
        private const val SEPARATOR = ":"
    }

    fun toUser() = User(id.value, email, authorities, password)
}

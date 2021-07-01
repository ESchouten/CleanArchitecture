package com.erikschouten.cleanarchitecture.repositories.user

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.domain.entity.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

internal object UserTable : IntIdTable() {
    val email = varchar("name", 50).index(isUnique = true)
    val authorities = text("authorities").nullable()
    val password = varchar("password", 200)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    var email by UserTable.email.transform({ email -> email.value }, { email -> Email(email) })
    var authorities by UserTable.authorities.transform(
        { authorities -> authorities.joinToString(SEPARATOR).takeIf { it.isNotBlank() } },
        { authorities -> authorities?.split(SEPARATOR)?.map { Authorities.valueOf(it) } ?: emptyList() }
    )
    var password by UserTable.password.transform({ password -> password.value }, { password -> PasswordHash(password) })

    companion object : IntEntityClass<UserEntity>(UserTable) {
        private const val SEPARATOR = ":"
    }

    fun toUser() = User(id.value, email, authorities, password)
}

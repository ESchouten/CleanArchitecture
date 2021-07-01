package com.erikschouten.cleanarchitecture.repositories.user

import com.erikschouten.cleanarchitecture.domain.entity.user.Authorities
import com.erikschouten.cleanarchitecture.domain.entity.user.Email
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.domain.entity.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

internal object UserTable : IntIdTable() {
    val email = varchar("name", 50).index(isUnique = true)
    val password = varchar("password", 200)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    var email by UserTable.email.transform({ email -> email.value }, { email -> Email(email) })
    val authorities by AuthorityEntity referrersOn AuthorityTable.user
    var password by UserTable.password.transform({ password -> password.value }, { password -> PasswordHash(password) })

    companion object : IntEntityClass<UserEntity>(UserTable)

    fun toUser() = User(id.value, email, authorities.map { it.authority }, password)
}

internal object AuthorityTable : IntIdTable() {
    val user = reference("user", UserTable)
    val authority = varchar("authority", 255)
}

class AuthorityEntity(id: EntityID<Int>) : IntEntity(id) {
    var user by UserEntity referencedOn AuthorityTable.user
    var authority by AuthorityTable.authority.transform({ authority -> authority.name }, { authority -> Authorities.valueOf(authority) })

    companion object : IntEntityClass<AuthorityEntity>(AuthorityTable)
}

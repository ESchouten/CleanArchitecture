package repositories.user

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

internal object UserTable : IntIdTable() {
    val email = varchar("email", 50).index(isUnique = true)
    val password = varchar("password", 200)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    var email by UserTable.email.transform({ it.value }, { Email(it) })
    val authorities by AuthorityEntity referrersOn AuthorityTable.user
    var password by UserTable.password.transform({ it.value }, { PasswordHash(it) })

    companion object : IntEntityClass<UserEntity>(UserTable)

    fun toUser() = User(id.value, email, authorities.map { it.authority }, password)
}

internal object AuthorityTable : IntIdTable() {
    val user = reference("user", UserTable)
    val authority = varchar("authority", 255)
}

class AuthorityEntity(id: EntityID<Int>) : IntEntity(id) {
    var user by UserEntity referencedOn AuthorityTable.user
    var authority by AuthorityTable.authority.transform({ it.name }, { Authorities.valueOf(it) })

    companion object : IntEntityClass<AuthorityEntity>(AuthorityTable)
}

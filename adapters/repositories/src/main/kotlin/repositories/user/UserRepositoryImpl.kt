package repositories.user

import domain.AlreadyExistsException
import domain.entity.user.Email
import domain.entity.user.User
import domain.repository.Order
import domain.repository.Pagination
import domain.repository.PaginationResult
import domain.repository.UserRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import repositories.DefaultDAO
import repositories.order
import repositories.query
import repositories.search

class UserRepositoryImpl : UserRepository, DefaultDAO<User, Int, UserEntity>(UserEntity) {

    override suspend fun findAll(pagination: Pagination) = query {
        val userColumns = listOf(UserTable.email, UserTable.id)
        val query = pagination.search?.let {
            SqlExpressionBuilder.search(UserTable to userColumns, it)?.let {
                UserEntity.find { it }
            }
        } ?: UserEntity.all()
        PaginationResult(
            query.copy()
                .limit(pagination.itemsPerPage, pagination.offset())
                .order(listOf(UserTable.id, UserTable.email), pagination.sort, UserTable.email to Order.ASC)
                .map { it.toDomain() },
            query.copy().count()
        )
    }

    override suspend fun findByEmail(email: Email) = query {
        UserEntity.find { UserTable.email eq email.value }.firstOrNull()?.toUser()
    }

    override suspend fun create(entity: User) = query {
        val user = UserEntity.new {
            email = entity.email
            password = entity.password
        }

        entity.authorities.forEach {
            AuthorityEntity.new {
                this.user = user
                authority = it
            }
        }

        user.toUser()
    }

    override suspend fun update(entity: User) = query {
        val user = UserEntity[entity.id].apply {
            email = entity.email
            password = entity.password
        }

        val currentAuthorities = AuthorityEntity.find { AuthorityTable.user eq user.id }.toMutableList()

        entity.authorities.forEach { authority ->
            val current = currentAuthorities.find { it.authority == authority }
            if (current == null) {
                AuthorityEntity.new {
                    this.user = user
                    this.authority = authority
                }
            } else currentAuthorities.remove(current)
        }

        currentAuthorities.forEach { it.delete() }

        user.toUser()
    }

    override fun UserEntity.toDomain() = this.toUser()
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<Int, User>()

    override suspend fun findById(id: Int) = users[id]
    override suspend fun findAll() = users.values.toList()
    override suspend fun create(entity: User): User {
        if (users.containsKey(entity.id)) throw AlreadyExistsException()
        return update(entity)
    }

    override suspend fun update(entity: User): User {
        users[entity.id] = entity
        return entity
    }

    override suspend fun delete(id: Int) {
        users.remove(id)
    }

    override suspend fun count(pagination: Pagination?) =
        (pagination?.search?.let { search ->
            users.values.filter { it.email.value.contains(search) }
        } ?: users.values.toList()).size.toLong()

    override suspend fun findByEmail(email: Email) = users.values.find { it.email == email }
    override suspend fun findAll(pagination: Pagination): PaginationResult<User> {
        val users = pagination.search?.let { search ->
            users.values.filter { it.email.value.contains(search) }
        } ?: users.values.toList()
        return PaginationResult(
            users.slice(pagination.offset().toInt()..pagination.offset().toInt() + pagination.itemsPerPage),
            users.size.toLong()
        )
    }
}

package repositories

import com.benasher44.uuid.Uuid
import entities.User

interface UserRepository : Repository<User, Uuid> {
    fun findByEmail(email: String): User?
}

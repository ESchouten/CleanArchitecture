import com.benasher44.uuid.Uuid
import entities.Authorities
import io.ktor.auth.*
import models.UserModel

class UserPrincipal private constructor(
    id: Uuid,
    email: String,
    authorities: List<Authorities>,
) : UserModel(id, email, authorities), Principal

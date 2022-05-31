package authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import usecases.dependency.Authenticator
import usecases.model.UserModel
import java.util.*

class JWTAuthenticatorImpl(
    private val issuer: String,
    secret: String?,
) : Authenticator {
    private val algorithm: Algorithm = Algorithm.HMAC256(secret ?: UUID.randomUUID().toString())

    val verifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()!!

    override fun generate(user: UserModel) = JWT
        .create()
        .withIssuer(issuer)
        .withSubject(user.id.toString())
        .withClaim("email", user.email.value)
        .withArrayClaim("authorities", user.authorities.map { it.name }.toTypedArray())
        .sign(algorithm)!!
}

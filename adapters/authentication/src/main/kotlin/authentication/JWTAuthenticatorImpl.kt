package authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import usecases.dependency.Authenticator
import usecases.model.UserModel

class JWTAuthenticatorImpl(
    private val issuer: String,
    val realm: String,
    secret: String,
) : Authenticator {
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)

    val audience = "Users"

    val verifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()!!

    override fun generate(user: UserModel) = JWT
        .create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withSubject(user.id.toString())
        .withClaim("email", user.email.value)
        .withArrayClaim("authorities", user.authorities.map { it.name }.toTypedArray())
        .sign(algorithm)!!
}

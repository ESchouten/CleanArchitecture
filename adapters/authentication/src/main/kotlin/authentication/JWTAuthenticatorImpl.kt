package authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import usecases.dependency.Authenticator

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

    override fun generate(id: Int) = JWT
        .create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withSubject(id.toString())
        .sign(algorithm)!!
}

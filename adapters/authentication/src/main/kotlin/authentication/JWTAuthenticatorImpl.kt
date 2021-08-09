package authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import domain.entity.user.Authorities
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

    override fun generate(id: Int, authorities: List<Authorities>) = JWT
        .create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withSubject(id.toString())
        .withArrayClaim("authorities", authorities.map { it.name }.toTypedArray())
        .sign(algorithm)!!
}

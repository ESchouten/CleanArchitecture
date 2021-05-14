package com.erikschouten.cleanarchitecture.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.erikschouten.cleanarchitecture.usecases.dependency.Authenticator

class JWTAuthenticatorImpl(
    private val issuer: String,
    val audience: String,
    val realm: String,
    secret: String = uuid4().toString(),
) : Authenticator {
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)

    val verifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()!!

    override fun generate(id: Uuid) = JWT
        .create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withSubject(id.toString())
        .sign(algorithm)!!
}

package ktor

import authentication.JWTAuthenticatorImpl
import config.Config
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.Password
import domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktor.plugins.get
import usecases.dependency.Authenticator
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.user.LoginUser

private const val AUTH_COOKIE = "JWT"

class UserPrincipal private constructor(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities>,
) : Principal {
    constructor(user: UserModel) : this(user.id, user.email, user.authorities)

    fun toUserModel() = UserModel(id, email, authorities, false)
}

fun Application.loginModule(config: Config) {

    install(ForwardedHeaderSupport)
    install(Authentication) {
        jwt {
            authHeader {
                it.request.cookies[AUTH_COOKIE]?.let { parseAuthorizationHeader(it) }
                    ?: it.request.parseAuthorizationHeader()
            }
            val authenticator = get<Authenticator>() as JWTAuthenticatorImpl
            val userRepository = get<UserRepository>()
            verifier(authenticator.verifier)
            validate { credential ->
                userRepository.findById(credential.payload.subject.toInt())?.let {
                    UserPrincipal(UserModel(it))
                }
            }
        }
    }

    routing {
        post("/login") {
            try {
                val login = call.receive<Map<String, String>>()
                    .let { LoginUserModel(Email(it["email"]!!), Password(it["password"]!!)) }
                val loginUser = this@routing.get<LoginUser>()
                call.response.cookies.append(
                    Cookie(
                        AUTH_COOKIE,
                        "Bearer " + loginUser(null, login),
                        httpOnly = true,
                        secure = !config.development,
                        extensions = mapOf("SameSite" to "lax")
                    )
                )
                call.respond(HttpStatusCode.OK)
            } catch (ex: Exception) {
                ex.printStackTrace()
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/logout") {
            call.response.cookies.appendExpired(AUTH_COOKIE)
            call.respond(HttpStatusCode.OK)
        }
    }
}

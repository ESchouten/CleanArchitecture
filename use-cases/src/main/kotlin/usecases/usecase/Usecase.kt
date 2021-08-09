package usecases.usecase

import domain.AuthorizationException
import domain.LoginException
import domain.entity.user.Authorities
import usecases.model.UserModel
import kotlin.reflect.KType

@Target(AnnotationTarget.CLASS)
annotation class Query

@Target(AnnotationTarget.CLASS)
annotation class Mutation

sealed class UsecaseType<R : Any>(
    val result: KType
) {
    abstract val authorities: List<Authorities>
    abstract val args: List<KType>
    open val authenticated = true
    fun auth(authentication: UserModel?): UserModel? =
        if (authenticated) {
            if (authentication == null) authentication ?: throw LoginException()
            if (authorities.isNotEmpty() && !authorities.any { it in authentication.authorities }) {
                throw AuthorizationException()
            }
            authentication
        } else authentication
}

abstract class UsecaseA0<R : Any>(
    result: KType
) : UsecaseType<R>(result) {

    final override val args get() = emptyList<KType>()
    abstract val executor: suspend (authentication: UserModel?) -> R
    suspend operator fun invoke(authentication: UserModel?) = executor(auth(authentication))
}

abstract class UsecaseA1<A0 : Any, R : Any>(
    private val a0: KType,
    result: KType
) : UsecaseType<R>(result) {

    final override val args get() = listOf(a0)
    abstract val executor: suspend (authentication: UserModel?, a0: A0) -> R
    suspend operator fun invoke(authentication: UserModel?, a0: A0) = executor(auth(authentication), a0)
}

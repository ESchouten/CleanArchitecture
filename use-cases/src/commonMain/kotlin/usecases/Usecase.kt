package usecases

import models.UserModel
import kotlin.reflect.KClass

annotation class Usecase
sealed class UsecaseType

abstract class UsecaseA0<R : Any>(
    val result: KClass<R>
) : UsecaseType() {
    abstract val executor: (authentication: UserModel?) -> R
    fun execute(authentication: UserModel?) = executor(authentication)
}

abstract class UsecaseA1<A0 : Any, R : Any>(
    val a0: KClass<A0>,
    val result: KClass<R>
) : UsecaseType() {
    abstract val executor: (authentication: UserModel?, a0: A0) -> R
    fun execute(authentication: UserModel?, request: A0) = executor(authentication, request)
}

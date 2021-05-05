package usecases

import models.UserModel
import kotlin.reflect.KClass

annotation class Usecase
sealed class UsecaseType

abstract class UsecaseA0<Result : Any>(
    val result: KClass<Result>
) : UsecaseType() {
    abstract val executor: (authentication: UserModel?) -> Result
    fun execute(authentication: UserModel?) = executor(authentication)
}

abstract class UsecaseA1<Request : Any, Result : Any>(
    val a0: KClass<Request>,
    val result: KClass<Result>
) : UsecaseType() {
    abstract val executor: (a0: Request, authentication: UserModel?) -> Result
    fun execute(request: Request, authentication: UserModel?) = executor(request, authentication)
}

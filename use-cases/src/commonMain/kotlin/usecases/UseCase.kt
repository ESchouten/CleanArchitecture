package usecases

import models.UserModel
import kotlin.reflect.KClass

abstract class UseCase<Request: Any, Result: Any>(
    val requestType: KClass<Request>,
    val resultType: KClass<Result>
) {

    abstract val executor: (request: Request, authentication: UserModel?) -> Result
    fun execute(request: Request, authentication: UserModel?): Result = executor(request, authentication)
}

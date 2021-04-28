package usecases

import models.UserModel

interface UseCase<Request, Result> {
    val executor: (request: Request, authentication: UserModel?) -> Result
    fun execute(request: Request, authentication: UserModel?) = executor(request, authentication)
}

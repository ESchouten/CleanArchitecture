package usecases

abstract class UseCase<Request, Result> {
    abstract fun execute(request: Request): Result
}

abstract class Validator<Request> {
    abstract fun validate(request: Request)
}

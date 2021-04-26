package usecases

import EmailAlreadyExistsException
import EmailInvalidException
import PasswordInvalidException
import dtos.CreateUserModel
import dtos.UserModel
import email
import entities.User
import password
import repositories.UserRepository

data class CreateUser(
    private val repository: UserRepository
) : UseCase<CreateUserModel, UserModel> {

    override fun validate(request: CreateUserModel) {
        if (!email(request.email)) throw EmailInvalidException()
        if (!password(request.password)) throw PasswordInvalidException()
        if (UserExists(request.email, repository).execute()) throw EmailAlreadyExistsException()
    }

    override fun execute(request: CreateUserModel): UserModel {
        validate(request)
        return UserModel.of(repository.save(User(request.email, request.password)))
    }
}

data class CreateUserValidator : Validator<CreateUserModel>() {
    override fun validate(request: CreateUserModel) {
        TODO("Not yet implemented")
    }
}

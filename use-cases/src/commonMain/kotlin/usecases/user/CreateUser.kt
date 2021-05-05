package usecases.user

import AuthorizationException
import EmailAlreadyExistsException
import EmailInvalidException
import PasswordInvalidException
import email
import entities.Authorities
import models.CreateUserModel
import models.UserModel
import password
import repositories.UserRepository
import usecases.UseCase

data class CreateUser(
    private val repository: UserRepository
) : UseCase<CreateUserModel, UserModel>(CreateUserModel::class, UserModel::class) {

    override val executor = { request: CreateUserModel, authentication: UserModel? ->
        if (authentication == null || !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        if (!email(request.email)) throw EmailInvalidException()
        if (!password(request.password)) throw PasswordInvalidException()
        if (UserExists(repository).execute(request.email, authentication)) throw EmailAlreadyExistsException()
        /** TODO: BCrypt **/
        UserModel.of(repository.save(request.toUser()))
    }
}
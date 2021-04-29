package usecases.user

import AuthorizationException
import EmailAlreadyExistsException
import EmailInvalidException
import PasswordInvalidException
import email
import entities.Authorities
import entities.User
import models.CreateUserModel
import models.UserModel
import password
import repositories.UserRepository
import usecases.UseCase

data class CreateUser(
    private val repository: UserRepository
) : UseCase<CreateUserModel, UserModel> {

    override val executor = { request: CreateUserModel, authentication: UserModel? ->
        if (authentication == null || !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        if (!email(request.email)) throw EmailInvalidException()
        if (!password(request.password)) throw PasswordInvalidException()
        /** TODO: BCrypt **/
        if (UserExists(repository).execute(request.email, authentication)) throw EmailAlreadyExistsException()
        UserModel.of(repository.save(User(request.email, request.authorities, request.password)))
    }
}

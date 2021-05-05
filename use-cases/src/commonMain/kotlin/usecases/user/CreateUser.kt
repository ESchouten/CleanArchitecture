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
import usecases.Usecase
import usecases.UsecaseA1

@Usecase
class CreateUser(
    private val repository: UserRepository
) : UsecaseA1<CreateUserModel, UserModel>(CreateUserModel::class, UserModel::class) {

    override val executor = { authentication: UserModel?, a0: CreateUserModel ->
        if (authentication == null || !authentication.authorities.contains(Authorities.USER)) throw AuthorizationException()
        if (!email(a0.email)) throw EmailInvalidException()
        if (!password(a0.password)) throw PasswordInvalidException()
        if (UserExists(repository).execute(authentication, a0.email)) throw EmailAlreadyExistsException()
        /** TODO: BCrypt **/
        UserModel.of(repository.save(a0.toUser()))
    }
}

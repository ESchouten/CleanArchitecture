open class AlreadyExistsException(message: String = "Already exists") : Exception(message)
class EmailAlreadyExistsException(message: String = "Email already in use") : AlreadyExistsException(message)

open class InvalidPropertyException(message: String = "Invalid property") : Exception(message)
class EmailInvalidException(message: String = "Email invalid") : InvalidPropertyException(message)
class PasswordInvalidException(message: String = "Password invalid") : InvalidPropertyException(message)

open class AuthorizationException(message: String = "No authority for this action") : Exception(message)
class LoginException(message: String = "Invalid authentication") : AuthorizationException(message)

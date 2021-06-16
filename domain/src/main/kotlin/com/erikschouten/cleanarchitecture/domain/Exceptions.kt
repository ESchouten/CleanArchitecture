package com.erikschouten.cleanarchitecture.domain

open class AlreadyExistsException(message: String? = null, base: String = "Already exists") : Exception(message(base, message))
class EmailAlreadyExistsException(message: String? = null, base: String = "Email already in use") : AlreadyExistsException(message, base)

open class InvalidPropertyException(message: String? = null, base: String = "Invalid property") : Exception(message(base, message))
class EmailInvalidException(message: String? = null, base: String = "Email invalid") : InvalidPropertyException(message, base)
class PasswordInvalidException(message: String? = null, base: String = "Password invalid") : InvalidPropertyException(message, base)

open class AuthorizationException(message: String? = null, base: String = "No authority for this action") : Exception(message(base, message))
class LoginException(message: String? = null, base: String = "Invalid authentication") : AuthorizationException(message, base)

open class NotFoundException(message: String? = null, base: String = "Not found") : Exception(message(base, message))
class UserNotFoundException(message: String? = null, base: String = "User not found") : NotFoundException(message, base)

private fun message(base: String, message: String?) = base + if (message != null) ": $message" else ""

package com.erikschouten.cleanarchitecture.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder

class PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: String) = BCrypt.withDefaults().hashToString(12, password.toCharArray())!!
    override fun matches(password: String, hash: String) = BCrypt.verifyer().verify(password.toCharArray(), hash).verified
}

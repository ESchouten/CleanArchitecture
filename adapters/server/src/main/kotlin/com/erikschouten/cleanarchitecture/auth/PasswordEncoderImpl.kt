package com.erikschouten.cleanarchitecture.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.erikschouten.cleanarchitecture.dependency.PasswordEncoder
import com.erikschouten.cleanarchitecture.entity.Password
import com.erikschouten.cleanarchitecture.entity.PasswordHash

class PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: Password) = PasswordHash(BCrypt.withDefaults().hashToString(12, password.value.toCharArray()))
    override fun matches(password: Password, hash: PasswordHash) = BCrypt.verifyer().verify(password.value.toCharArray(), hash.value).verified
}

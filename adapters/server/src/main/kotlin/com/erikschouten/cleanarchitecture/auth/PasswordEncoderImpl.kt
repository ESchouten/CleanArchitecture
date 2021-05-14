package com.erikschouten.cleanarchitecture.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.erikschouten.cleanarchitecture.domain.entity.Password
import com.erikschouten.cleanarchitecture.domain.entity.PasswordHash
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder

class PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: Password) = PasswordHash(BCrypt.withDefaults().hashToString(12, password.value.toCharArray()))
    override fun matches(password: Password, hash: PasswordHash) = BCrypt.verifyer().verify(password.value.toCharArray(), hash.value).verified
}

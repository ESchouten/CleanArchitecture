package com.erikschouten.cleanarchitecture.authentication

import at.favre.lib.crypto.bcrypt.BCrypt
import com.erikschouten.cleanarchitecture.domain.entity.user.Password
import com.erikschouten.cleanarchitecture.domain.entity.user.PasswordHash
import com.erikschouten.cleanarchitecture.usecases.dependency.PasswordEncoder

class PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: Password) =
        PasswordHash(BCrypt.withDefaults().hashToString(12, password.value.toCharArray()))

    override fun matches(password: Password, hash: PasswordHash) =
        BCrypt.verifyer().verify(password.value.toCharArray(), hash.value).verified
}

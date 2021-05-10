package com.erikschouten.cleanarchitecture

import com.benasher44.uuid.Uuid

interface Authenticator {
    fun generate(id: Uuid): String
}

fun email(email: String) = email.contains('@')
fun password(password: String) =
    password.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"))

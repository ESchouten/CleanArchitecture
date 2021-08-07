package com.erikschouten.cleanarchitecture.usecases

import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseType

interface UsecaseTests {
    val usecase: UsecaseType<*>

    fun success()
    fun unauthenticated()
    fun `No user roles`()
}

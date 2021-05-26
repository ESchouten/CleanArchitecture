package com.erikschouten.cleanarchitecture.usecases.usecase

import com.erikschouten.cleanarchitecture.domain.LoginException
import com.erikschouten.cleanarchitecture.usecases.model.UserModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Query

@Target(AnnotationTarget.CLASS)
annotation class Mutation

sealed class UsecaseType<R : Any>(
    val result: KClass<R>
) {
    abstract val args: List<KClass<*>>
    open val authenticated = true
    fun auth(authentication: UserModel?): UserModel? =
        if (authenticated) authentication ?: throw LoginException() else authentication
}

abstract class UsecaseA0<R : Any>(
    result: KClass<R>
) : UsecaseType<R>(result) {

    final override val args get() = emptyList<KClass<*>>()
    abstract val executor: suspend (authentication: UserModel?) -> R
    suspend operator fun invoke(authentication: UserModel?) = executor(auth(authentication))
}

abstract class UsecaseA1<A0 : Any, R : Any>(
    private val a0: KClass<A0>,
    result: KClass<R>
) : UsecaseType<R>(result) {

    final override val args get() = listOf(a0)
    abstract val executor: suspend (authentication: UserModel?, a0: A0) -> R
    suspend operator fun invoke(authentication: UserModel?, a0: A0) = executor(auth(authentication), a0)
}

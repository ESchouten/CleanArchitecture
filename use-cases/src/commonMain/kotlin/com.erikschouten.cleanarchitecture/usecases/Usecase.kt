package com.erikschouten.cleanarchitecture.usecases

import com.erikschouten.cleanarchitecture.models.UserModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Query

@Target(AnnotationTarget.CLASS)
annotation class Mutation

sealed class UsecaseType<R : Any>(
    val result: KClass<R>
) {
    abstract val args: List<KClass<*>>
}

abstract class UsecaseA0<R : Any>(
    result: KClass<R>
) : UsecaseType<R>(result) {

    final override val args get() = emptyList<KClass<*>>()
    abstract val executor: (authentication: UserModel?) -> R
    operator fun invoke(authentication: UserModel?) = executor(authentication)
}

abstract class UsecaseA1<A0 : Any, R : Any>(
    private val a0: KClass<A0>,
    result: KClass<R>
) : UsecaseType<R>(result) {

    final override val args get() = listOf(a0)
    abstract val executor: (authentication: UserModel?, a0: A0) -> R
    operator fun invoke(authentication: UserModel?, a0: A0) = executor(authentication, a0)
}

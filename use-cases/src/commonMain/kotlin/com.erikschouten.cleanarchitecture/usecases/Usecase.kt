package com.erikschouten.cleanarchitecture.usecases

import com.erikschouten.cleanarchitecture.models.UserModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Usecase

@Usecase
@Target(AnnotationTarget.CLASS)
annotation class Query

@Usecase
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
    fun execute(authentication: UserModel?) = executor(authentication)
}

abstract class UsecaseA1<A0 : Any, R : Any>(
    val a0: KClass<A0>,
    result: KClass<R>
) : UsecaseType<R>(result) {

    final override val args get() = listOf(a0)
    abstract val executor: (authentication: UserModel?, a0: A0) -> R
    fun execute(authentication: UserModel?, a0: A0) = executor(authentication, a0)
}

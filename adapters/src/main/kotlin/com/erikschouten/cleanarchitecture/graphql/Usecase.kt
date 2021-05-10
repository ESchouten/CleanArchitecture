package com.erikschouten.cleanarchitecture.graphql

import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.schema.Publisher
import com.apurebase.kgraphql.schema.dsl.ResolverDSL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.operations.AbstractOperationDSL
import com.apurebase.kgraphql.schema.model.InputValueDef
import com.apurebase.kgraphql.schema.model.MutableSchemaDefinition
import com.apurebase.kgraphql.schema.model.TypeDef
import com.erikschouten.cleanarchitecture.auth.UserPrincipal
import com.erikschouten.cleanarchitecture.usecases.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

fun SchemaBuilder.usecases(usecases: Array<UsecaseType<*>>) {
    val types = mutableSetOf<KClass<*>>()
    usecases.forEach {
        usecase(it)
        types.addAll(types(it))
    }

    val scalars: List<KClass<*>> = this
        .privateProperty<SchemaBuilder, MutableSchemaDefinition>("model")
        .privateProperty<MutableSchemaDefinition, ArrayList<TypeDef.Scalar<*>>>("scalars")
        .map { it.kClass }

    types(types, scalars).forEach { type(it) }
}

fun SchemaBuilder.usecase(usecase: UsecaseType<*>) {
    val type: ((String, AbstractOperationDSL.() -> Unit) -> Publisher)? = usecase::class.let {
        when {
            it.hasAnnotation<Query>() -> ::query
            it.hasAnnotation<Mutation>() -> ::mutation
            else -> null
        }
    }

    type?.invoke(usecase::class.simpleName!!) {
        when (usecase) {
            is UsecaseA0<*> -> usecase(usecase)
            is UsecaseA1<*, *> -> usecase(usecase)
            else -> throw Exception("Invalid com.erikschouten.cleanarchitecture.graphql.usecase")
        }.apply {
            setReturnType(usecase.result.createType())
            addInputValues(usecase.args.mapIndexed { index, kClass -> InputValueDef(kClass, "a${index}") })
        }
    }
}

fun <R, U : UsecaseA0<R>> AbstractOperationDSL.usecase(usecase: U): ResolverDSL {
    return resolver { ctx: Context ->
        usecase(ctx.get<UserPrincipal>()?.toUserModel())
    }
}

fun <R, A0, U : UsecaseA1<A0, R>> AbstractOperationDSL.usecase(usecase: U): ResolverDSL {
    return resolver { ctx: Context, a0: A0 ->
        usecase(ctx.get<UserPrincipal>()?.toUserModel(), a0)
    }
}

fun types(usecase: UsecaseType<*>): List<KClass<*>> {
    return usecase.args + listOf(usecase.result)
}

fun types(types: Set<KClass<*>>, scalars: List<KClass<*>>): Set<KClass<*>> {
    val found = types.filterNotTo(mutableSetOf()) { it in scalars }
    found.addAll(
        found.flatMap {
            val props = properties(it)
            props.flatMap { types(props.toSet(), scalars) }
        }
    )
    return found.toSet()
}

fun SchemaBuilder.type(type: KClass<*>) {
    if (type.isSubclassOf(Enum::class)) {
        enum(type as KClass<Enum<*>>)
    } else type(type) {}
}

fun <T : Enum<T>> SchemaBuilder.enum(type: KClass<T>) {
    enum(kClass = type, enumValues = type.java.enumConstants as Array<T>, block = null)
}

inline fun <reified T : Any, R> T.privateProperty(name: String): R =
    T::class
        .memberProperties
        .first { it.name == name }
        .apply { isAccessible = true }
        .get(this) as R

fun properties(type: KClass<*>): List<KClass<*>> {
    return type.memberProperties.map { it.returnType }.map {
        if (it.isCollection()) {
            it.arguments.first().type!!.classifier as KClass<*>
        } else {
            it.classifier as KClass<*>
        }
    }
}

fun KType.isCollection() = (this.classifier as KClass<*>).supertypes.map { superType -> superType.jvmErasure }
    .any { kClass -> kClass == Collection::class.starProjectedType.jvmErasure || kClass == Array::class.starProjectedType.jvmErasure }

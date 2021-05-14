package com.erikschouten.cleanarchitecture.graphql

import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.schema.Publisher
import com.apurebase.kgraphql.schema.dsl.ResolverDSL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.operations.AbstractOperationDSL
import com.apurebase.kgraphql.schema.model.InputValueDef
import com.apurebase.kgraphql.schema.model.MutableSchemaDefinition
import com.apurebase.kgraphql.schema.model.TypeDef
import com.erikschouten.cleanarchitecture.usecases.usecase.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.full.*

fun schema(usecases: Array<UsecaseType<*>>): SchemaBuilder.() -> Unit = {
    stringScalar<UUID> {
        deserialize = { id: String -> UUID.fromString(id) }
        serialize = UUID::toString
    }

    usecases(usecases)
}

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
            else -> throw Exception("Invalid usecase")
        }.apply {
            setReturnType(usecase.result.createType())
            addInputValues(usecase.args.mapIndexed { index, kClass -> InputValueDef(kClass, "a${index}") })
        }
    }
}

fun <R, U : UsecaseA0<R>> AbstractOperationDSL.usecase(usecase: U): ResolverDSL {
    return resolver { ctx: Context ->
        usecase(ctx.get())
    }
}

fun <R, A0, U : UsecaseA1<A0, R>> AbstractOperationDSL.usecase(usecase: U): ResolverDSL {
    return resolver { ctx: Context, a0: A0 ->
        usecase(ctx.get(), a0)
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

@Suppress("UNCHECKED_CAST")
fun SchemaBuilder.type(type: KClass<*>) {
    if (type.isSubclassOf(Enum::class)) {
        enum(type as KClass<Enum<*>>)
    } else type(type) {}
}

fun <T : Enum<T>> SchemaBuilder.enum(type: KClass<T>) {
    enum(kClass = type, enumValues = type.java.enumConstants as Array<T>, block = null)
}

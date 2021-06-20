package com.erikschouten.cleanarchitecture.graphql

import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.schema.Publisher
import com.apurebase.kgraphql.schema.dsl.ResolverDSL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.operations.AbstractOperationDSL
import com.apurebase.kgraphql.schema.dsl.types.ScalarDSL
import com.apurebase.kgraphql.schema.model.InputValueDef
import com.apurebase.kgraphql.schema.model.MutableSchemaDefinition
import com.apurebase.kgraphql.schema.model.TypeDef
import com.erikschouten.cleanarchitecture.domain.entity.ValueClass
import com.erikschouten.cleanarchitecture.usecases.usecase.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

fun usecases(usecases: Collection<UsecaseType<*>>): SchemaBuilder.() -> Unit = {
    stringScalar<UUID> {
        deserialize = { id: String -> UUID.fromString(id) }
        serialize = UUID::toString
    }

    usecases(usecases)
}

fun SchemaBuilder.usecases(usecases: Collection<UsecaseType<*>>) {
    val types = mutableSetOf<KClass<*>>()
    usecases.forEach {
        usecase(it)
        types.addAll(types(it))
    }

    val scalars: Set<KClass<*>> = this
        .privateProperty<SchemaBuilder, MutableSchemaDefinition>("model")
        .privateProperty<MutableSchemaDefinition, ArrayList<TypeDef.Scalar<*>>>("scalars")
        .map { it.kClass }
        .toSet()

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

fun types(types: Set<KClass<*>>, ignore: Set<KClass<*>>): Set<KClass<*>> {
    val found = types.filterNot { it in ignore }.toMutableSet()
    types.forEach {
        found.addAll(types(properties(it).toSet(), ignore + found))
    }
    return found.toSet()
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> SchemaBuilder.type(type: KClass<T>) {
    when {
        type.isSubclassOf(Enum::class) -> enum(type as KClass<Enum<*>>)
        type.isValue || type.supertypes.any { it.jvmErasure == ValueClass::class.starProjectedType.jvmErasure } -> valueClassScalar(type)
        else -> type(type) {}
    }
}

fun <T : Enum<T>> SchemaBuilder.enum(type: KClass<T>) {
    enum(kClass = type, enumValues = type.java.enumConstants as Array<T>, block = null)
}

fun <T : Any> SchemaBuilder.valueClassScalar(value: KClass<T>) {
    when (value.primaryConstructor!!.parameters.first().type) {
        String::class.starProjectedType -> stringScalar(value, scalar(value, String::class))
        Int::class.starProjectedType -> intScalar(value, scalar(value, Int::class))
        Long::class.starProjectedType -> longScalar(value, scalar(value, Long::class))
        Double::class.starProjectedType -> floatScalar(value, scalar(value, Double::class))
        Float::class.starProjectedType -> floatScalar(value, scalar(value, Double::class))
        Boolean::class.starProjectedType -> booleanScalar(value, scalar(value, Boolean::class))
        else -> throw Exception("Unsupported coercion")
    }
}

fun <S : Any, R : Any> scalar(scalar: KClass<S>, raw: KClass<R>): ScalarDSL<S, R>.() -> Unit {
    return {
        deserialize = { value: R -> scalar.constructors.first().call(value) }
        serialize = { value: S -> raw.cast(scalar.memberProperties.first().get(value)) }
    }
}

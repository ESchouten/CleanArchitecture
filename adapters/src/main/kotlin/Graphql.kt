import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.apurebase.kgraphql.schema.dsl.ResolverDSL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.operations.AbstractOperationDSL
import com.apurebase.kgraphql.schema.model.InputValueDef
import com.apurebase.kgraphql.schema.model.MutableSchemaDefinition
import com.apurebase.kgraphql.schema.model.TypeDef
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.auth.*
import models.LoginUserModel
import usecases.UsecaseA0
import usecases.UsecaseA1
import usecases.UsecaseType
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

fun GraphQL.Configuration.configure(usecases: Array<UsecaseType<*>>, development: Boolean = false) {
    this.playground = development

    wrap {
        authenticate(optional = true, build = it)
    }

    context { call ->
        call.authentication.principal<UserPrincipal>()?.let {
            +it
        }
    }

    schema {
        stringScalar<Uuid>{
            deserialize = { id: String -> uuidFrom(id) }
            serialize = Uuid::toString
        }

        usecases(usecases)
    }
}

fun <T : Enum<T>> SchemaBuilder.enum(type: KClass<T>) {
    enum(kClass = type, enumValues = type.java.enumConstants as Array<T>, block = null)
}

fun SchemaBuilder.usecases(usecases: Array<UsecaseType<*>>) {
    val types = mutableSetOf<KClass<*>>()
    usecases.forEach {
        usecase(it)
        types.addAll(types(it))
    }

    val scalars: List<KClass<*>> = this
        .getPrivateProperty<SchemaBuilder, MutableSchemaDefinition>("model")
        .getPrivateProperty<MutableSchemaDefinition, ArrayList<TypeDef.Scalar<*>>>("scalars")
        .map { it.kClass }

    types(types, scalars).forEach { type(it) }
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

fun properties(type: KClass<*>): List<KClass<*>> {
    return type.memberProperties.map { it.returnType }.map {
        if ((it.classifier as KClass<*>).supertypes.map { it.jvmErasure }.any { it == Collection::class.starProjectedType.jvmErasure || it == Array::class.starProjectedType.jvmErasure }) {
            it.arguments.first().type!!.classifier as KClass<*>
        } else {
            it.classifier as KClass<*>
        }
    }
}

fun SchemaBuilder.type(type: KClass<*>) {
    if (type.isSubclassOf(Enum::class)) {
        enum(type as KClass<Enum<*>>)
    } else type(type) {}
}

fun types(usecase: UsecaseType<*>): List<KClass<*>> {
    return usecase.args + listOf(usecase.result)
}

fun SchemaBuilder.usecase(usecase: UsecaseType<*>) {
    query(usecase::class.simpleName!!) {
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

fun <T, V : UsecaseA0<T>> AbstractOperationDSL.usecase(usecase: V): ResolverDSL {
    return resolver { ctx: Context ->
        usecase.execute(ctx.get<UserPrincipal>()?.toUserModel())
    }
}

fun <T, U, V : UsecaseA1<U, T>> AbstractOperationDSL.usecase(usecase: V): ResolverDSL {
    return resolver { ctx: Context, a0: U ->
        usecase.execute(ctx.get<UserPrincipal>()?.toUserModel(), a0)
    }
}

inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R =
    T::class
        .memberProperties
        .first { it.name == name }
        .apply { isAccessible = true }
        .get(this) as R

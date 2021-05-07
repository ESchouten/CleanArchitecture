import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.apurebase.kgraphql.schema.dsl.ResolverDSL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.operations.AbstractOperationDSL
import com.apurebase.kgraphql.schema.model.InputValueDef
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

fun GraphQL.Configuration.configure(usecases: Array<UsecaseType<*>>, types: Array<KClass<*>>, development: Boolean = false) {
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
    val scalars = model.toSchemaDefinition().scalars.map { it.kClass }
    types.filter {  }.forEach {
        if (it.isSubclassOf(Enum::class)) {
            enum(it as KClass<Enum<*>>)
        } else type(it) {}
    }
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
        usecase.execute(ctx.get<UserPrincipal>())
    }
}

fun <T, U, V : UsecaseA1<U, T>> AbstractOperationDSL.usecase(usecase: V): ResolverDSL {
    return resolver { ctx: Context, a0: U ->
        usecase.execute(ctx.get<UserPrincipal>(), a0)
    }
}

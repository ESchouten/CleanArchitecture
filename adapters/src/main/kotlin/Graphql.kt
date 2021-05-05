import com.apurebase.kgraphql.Context
import com.apurebase.kgraphql.GraphQL
import com.apurebase.kgraphql.schema.dsl.ResolverDSL
import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.apurebase.kgraphql.schema.dsl.operations.AbstractOperationDSL
import com.apurebase.kgraphql.schema.model.InputValueDef
import io.ktor.auth.*
import usecases.UsecaseA0
import usecases.UsecaseA1
import usecases.UsecaseType
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

fun GraphQL.Configuration.configure(usecases: List<UsecaseType<*>>, types: List<KClass<*>>, playground: Boolean = false) {
    this.playground = playground

    wrap {
        authenticate(optional = true, build = it)
    }

    context { call ->
        call.authentication.principal<UserPrincipal>()?.let {
            +it
        }
    }

    schema {
        usecases.forEach {
            usecase(it)
        }
        types.forEach {
            type(it) {}
        }
    }
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

fun <T : Any, V : UsecaseA0<T>> AbstractOperationDSL.usecase(usecase: V): ResolverDSL {
    return resolver { ctx: Context ->
        usecase.execute(ctx.get<UserPrincipal>()?.toUserModel())
    }
}

fun <T : Any, U : Any, V : UsecaseA1<U, T>> AbstractOperationDSL.usecase(usecase: V): ResolverDSL {
    return resolver { ctx: Context, a0: U ->
        usecase.execute(ctx.get<UserPrincipal>()?.toUserModel(), a0)
    }
}

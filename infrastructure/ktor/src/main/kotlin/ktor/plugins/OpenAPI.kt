package ktor.plugins

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import domain.AlreadyExistsException
import domain.AuthorizationException
import domain.LoginException
import domain.NotFoundException
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.core.routes.redoc
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import ktor.UserPrincipal
import usecases.usecase.*
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.Instant
import kotlin.reflect.KClass
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

fun Application.rest(usecases: Collection<UsecaseType<*>>) {
    routing {
        redoc(pageTitle = "CleanArchitecture API Docs")
        authenticate(optional = true) {
            usecases.filter { it::class.hasAnnotation<Query>() || it::class.hasAnnotation<Mutation>() }
                .forEach { usecase ->
                    val name = usecase::class.simpleName!!
                    route("/${name.lowercase()}") {
                        install(NotarizedRoute()) {
                            if (usecase is UsecaseA0<*> && usecase::class.hasAnnotation<Query>()) {
                                get = GetInfo.builder {
                                    configure(usecase)
                                }
                            } else {
                                post = PostInfo.builder {
                                    configure(usecase)
                                    request {
                                        description(name)
                                        when (usecase) {
                                            is UsecaseA1<*, *> ->
                                                RestA1::class.createType(usecase.args.map { KTypeProjection.invariant(it) })

                                            else -> null
                                        }.let {
                                            requestType(it ?: typeOf<Unit>())
                                        }
                                    }
                                }
                            }
                        }
                        if (usecase is UsecaseA0<*> && usecase::class.hasAnnotation<Query>()) {
                            get {
                                handle {
                                    execute(usecase)
                                }
                            }
                        } else {
                            post {
                                handle {
                                    when (usecase) {
                                        is UsecaseA0<*> -> execute(usecase)
                                        is UsecaseA1<*, *> -> execute(usecase)
                                        else -> throw Exception("Invalid usecase")
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.handle(execute: suspend () -> Any) {
    try {
        call.respond(execute())
    } catch (ex: LoginException) {
        call.respond(HttpStatusCode.Unauthorized, ex.message ?: "")
    } catch (ex: AuthorizationException) {
        call.respond(HttpStatusCode.Forbidden, ex.message ?: "")
    } catch (ex: AlreadyExistsException) {
        call.respond(HttpStatusCode.Conflict, ex.message ?: "")
    } catch (ex: NotFoundException) {
        call.respond(HttpStatusCode.NotFound, ex.message ?: "")
    } catch (ex: Exception) {
        call.respond(HttpStatusCode.InternalServerError, ex.message ?: "")
    }
}

private fun <T : MethodInfo> MethodInfo.Builder<T>.configure(usecase: UsecaseType<*>) {
    val name = usecase::class.simpleName!!
    summary(name)
    description(name)
    response {
        description(name)
        responseCode(HttpStatusCode.OK)
        responseType(usecase.result)
    }
}

fun ApplicationCall.getAuthentication() = authentication.principal<UserPrincipal>()?.user

suspend fun <R, U : UsecaseA0<R>> PipelineContext<Unit, ApplicationCall>.execute(usecase: U): R {
    return usecase(call.getAuthentication())
}

suspend fun <R, A0 : Any, U : UsecaseA1<A0, R>> PipelineContext<Unit, ApplicationCall>.execute(usecase: U): R {
    val args = call.deserialize<RestA1<A0>>(usecase)
    return usecase(call.getAuthentication(), args.a0)
}

suspend inline fun <reified T : Any> ApplicationCall.deserialize(usecase: UsecaseType<*>): T {
    val type = TypeToken.getParameterized(
        T::class.java,
        *usecase.args.map { boxedClassOf(it.jvmErasure).java }.toTypedArray()
    ).type
    return gson.fromJson(this.receiveText(), type)
}

fun boxedClassOf(kClass: KClass<*>) = when (kClass) {
    Boolean::class -> java.lang.Boolean::class
    Byte::class -> java.lang.Byte::class
    Char::class -> java.lang.Character::class
    Float::class -> java.lang.Float::class
    Int::class -> java.lang.Integer::class
    Long::class -> java.lang.Long::class
    Short::class -> java.lang.Short::class
    Double::class -> java.lang.Double::class
    else -> kClass
}

data class RestA1<A0>(
    val a0: A0
)

val gson = GsonBuilder().apply {
    registerTypeAdapter(Instant::class.java, InstantAdapter())
    registerTypeAdapter(BigDecimal::class.java, BigDecimalAdapter())
}.create()!!

class InstantAdapter : JsonSerializer<Instant>, JsonDeserializer<Instant> {
    override fun serialize(src: Instant, typeOfSrc: Type, context: JsonSerializationContext) =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        Instant.parse((json as JsonPrimitive).asString)
}

class BigDecimalAdapter : JsonSerializer<BigDecimal> {
    override fun serialize(src: BigDecimal, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toPlainString())
    }
}

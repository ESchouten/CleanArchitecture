package graphql

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
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

fun KType.isCollection() = (this.classifier as KClass<*>).allSuperclasses.any { kClass ->
    kClass == Collection::class || kClass == Array::class
}

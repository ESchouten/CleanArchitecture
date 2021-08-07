package graphql

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

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

fun KType.isCollection() = (this.classifier as KClass<*>).supertypes.map { superType -> superType.jvmErasure }
    .any { kClass -> kClass == Collection::class.starProjectedType.jvmErasure || kClass == Array::class.starProjectedType.jvmErasure }

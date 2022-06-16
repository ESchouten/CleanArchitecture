@file:Suppress("INVISIBLE_MEMBER")

package config

import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.instance.newInstance
import org.koin.core.module.Module
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier._q
import org.koin.java.KoinJavaComponent.getKoin
import org.reflections.Reflections
import usecases.usecase.UsecaseType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Any> getAll(): Collection<T> = getKoin().let { koin ->
    koin.instanceRegistry.instances.values.map { it.beanDefinition }.filter { it.kind == Kind.Singleton }
        .filter { it.primaryType.isSubclassOf(T::class) }
        .map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) }
}

fun Module.usecases(domain: String) {
    val pkg = "usecases.usecase.$domain"
    Reflections(pkg).getSubTypesOf(UsecaseType::class.java).map { it.kotlin }
        .filter { it.qualifiedName!!.startsWith(pkg) }.forEach { uc ->
            single(uc)
        }
}

fun Module.single(usecase: KClass<out UsecaseType<*>>): Pair<Module, SingleInstanceFactory<UsecaseType<*>>> {
    val def = BeanDefinition(
        _q("_root_"),
        usecase,
        null,
        { newInstance(usecase, emptyParametersHolder()) },
        Kind.Singleton,
        secondaryTypes = emptyList()
    )
    val factory = SingleInstanceFactory(def)
    indexPrimaryType(factory)
    return Pair(this, factory)
}
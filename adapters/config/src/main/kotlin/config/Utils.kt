@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package config

import domain.repository.Repository
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.instance.InstanceFactory
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.instance.newInstance
import org.koin.core.module.Module
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.java.KoinJavaComponent.getKoin
import org.reflections.Reflections
import usecases.usecase.UsecaseType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

private const val usecasePackage = "usecases.usecase"
private const val repositoryPackage = "repositories"
private val usecases get() = Reflections(usecasePackage).getSubTypesOf(UsecaseType::class.java)
private val repositories get() = Reflections(repositoryPackage).getSubTypesOf(Repository::class.java)

inline fun <reified T : Any> getAll(): Collection<T> = getKoin().let { koin ->
    koin.instanceRegistry.instances.values.map { it.beanDefinition }.filter { it.kind == Kind.Singleton }
        .filter { it.primaryType.isSubclassOf(T::class) }
        .map { koin.get(clazz = it.primaryType, qualifier = null, parameters = null) }
}

fun Module.usecasesAndRepositories(
    domain: String,
    excludeUsecases: List<KClass<UsecaseType<*>>> = emptyList(),
    excludeRepositories: List<KClass<out Repository<*, *>>> = emptyList()
) {
    usecases(domain, excludeUsecases)
    repositories(domain, excludeRepositories)
}

fun Module.usecases(domain: String, exclude: List<KClass<UsecaseType<*>>> = emptyList()) {
    usecases.map { it.kotlin }.filter { it.qualifiedName!!.startsWith("$usecasePackage.$domain") && !exclude.contains(it) }.forEach { uc ->
        usecase(uc)
    }
    single {}
}

fun Module.repositories(domain: String, exclude: List<KClass<out Repository<*, *>>> = emptyList()) {
    repositories.map { it.kotlin }.filter { it.qualifiedName!!.startsWith("$repositoryPackage.$domain") && !exclude.contains(it) }.forEach { uc ->
        repository(uc)
    }
}

fun Module.usecase(usecase: KClass<out UsecaseType<*>>): Pair<Module, SingleInstanceFactory<UsecaseType<*>>> {
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

fun Module.repository(repository: KClass<out Repository<*, *>>): Pair<Module, InstanceFactory<out Repository<*, *>>> {
    val def = BeanDefinition(
        _q("_root_"),
        repository,
        null,
        { newInstance(repository, emptyParametersHolder()) },
        Kind.Singleton,
        secondaryTypes = emptyList()
    )
    val factory = SingleInstanceFactory(def)
    indexPrimaryType(factory)
    val domain: KClass<in Repository<*, *>> =
        repository.supertypes.find { it.isSubtypeOf(typeOf<Repository<*, *>>()) }!!.jvmErasure as KClass<in Repository<*, *>>
    return Pair(this, factory).apply {
        bind(domain)
    }
}

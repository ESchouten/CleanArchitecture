import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import graphql.CustomDataFetcherFactory
import graphql.CustomValueUnboxer
import graphql.GraphQL
import graphql.Hooks
import graphql.schema.GraphQLSchema
import usecases.usecase.Mutation
import usecases.usecase.Query
import usecases.usecase.UsecaseType
import kotlin.reflect.full.hasAnnotation

/**
 * configuration for schema generation
 */
private val config = SchemaGeneratorConfig(supportedPackages = listOf("usecases", "domain"), hooks = Hooks(), dataFetcherFactoryProvider = CustomDataFetcherFactory())

/**
 * return the GraphQl Object
 */
fun getGraphQLObject(schema: GraphQLSchema): GraphQL = GraphQL.newGraphQL(schema)
    .valueUnboxer(CustomValueUnboxer())
    .build()

/**
 * queryNames and mutationNames contains the usecases class names -> see Hooks
 */
val queryNames = mutableListOf<String>()
val mutationNames = mutableListOf<String>()

/**
 * Function that build the graphql schema
 */
fun buildSchema(functions: Collection<UsecaseType<*>>): GraphQLSchema {
    val functionsPair = addClassNameToList(functions)
    return toSchema(config, functionsPair.first, functionsPair.second)
}

/**
 * add usecases class names to list and return two list (queryList and mutationList)
 */
private fun addClassNameToList(functions: Collection<UsecaseType<*>>): Pair<List<TopLevelObject>, List<TopLevelObject>> {
    val queryList = mutableListOf<TopLevelObject>()
    val mutationList = mutableListOf<TopLevelObject>()
    functions.forEach {
        when {
            it::class.hasAnnotation<Query>() -> {
                queryList.add(TopLevelObject(it))
                queryNames.add(it::class.simpleName!!)
            }
            it::class.hasAnnotation<Mutation>() -> {
                mutationList.add(TopLevelObject(it))
                mutationNames.add(it::class.simpleName!!)
            }
            else -> queryNames.add(it::class.simpleName!!)
        }
    }

    return Pair(queryList, mutationList)
}
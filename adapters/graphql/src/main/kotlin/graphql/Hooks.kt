package graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import domain.entity.user.Email
import domain.entity.user.NewPassword
import domain.entity.user.Password
import graphql.scalars.datetime.DateScalar
import graphql.schema.*
import mutationNames
import queryNames
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType


class Hooks: SchemaGeneratorHooks {
    /**
     * by default graphql-kotlin takes functions inside a class and build a query or a mutation with the function name
     * we have to change the query or mutation name and the dataFetcher coordinates to match the class name
     *
     * graphql-kotlin add query first and then mutations in the order they are sent in the toSchema function, this behaviour allows us to change the names by
     *  getting every time the first element from the lists previously generated
     */
    override fun onRewireGraphQLType(
        generatedType: GraphQLSchemaElement,
        coordinates: FieldCoordinates?,
        codeRegistry: GraphQLCodeRegistry.Builder
    ): GraphQLSchemaElement {
        if (generatedType is GraphQLFieldDefinition && generatedType.name == "invoke") {
            val newName = if(queryNames.isNotEmpty()) queryNames.removeFirst() else mutationNames.removeFirst()

            val newGeneratedType = generatedType.transform { it.name(newName) }
            val newCoordinates = FieldCoordinates.coordinates(coordinates!!.typeName, newName)
            val dataFetcher = codeRegistry.getDataFetcher(coordinates, generatedType)
            val newCodeRegistry = codeRegistry.dataFetcher(newCoordinates, dataFetcher)

            return super.onRewireGraphQLType(newGeneratedType, newCoordinates, newCodeRegistry)
        }

        return super.onRewireGraphQLType(generatedType, coordinates, codeRegistry)
    }

    /**
     * exclude ValueClass from schema
     */
    override fun isValidSuperclass(kClass: KClass<*>): Boolean {
        if (kClass.simpleName.equals("ValueClass"))
            return false

        return super.isValidSuperclass(kClass)
    }

    /**
     * accept only invoke functions, other functions are ignored and excluded from schema
     */
    override fun isValidFunction(kClass: KClass<*>, function: KFunction<*>): Boolean {
        return function.name == "invoke"
    }

    /**
     * remove argument authentication from queries
     */
    override fun didGenerateQueryField(
        kClass: KClass<*>,
        function: KFunction<*>,
        fieldDefinition: GraphQLFieldDefinition
    ): GraphQLFieldDefinition {
        val newArguments = fieldDefinition.arguments.toMutableList()
        newArguments.removeAt(0)
        val newFieldDefinition = fieldDefinition.transform { it.replaceArguments(newArguments) }

        return super.didGenerateQueryField(kClass, function, newFieldDefinition)
    }

    /**
     * remove argument authentication from mutations
     */
    override fun didGenerateMutationField(
        kClass: KClass<*>,
        function: KFunction<*>,
        fieldDefinition: GraphQLFieldDefinition
    ): GraphQLFieldDefinition {
        val newArguments = fieldDefinition.arguments.toMutableList()
        newArguments.removeAt(0)
        val newFieldDefinition = fieldDefinition.transform { it.replaceArguments(newArguments) }

        return super.didGenerateMutationField(kClass, function, newFieldDefinition)
    }

    /**
     * generate value class and additional graphql types
     */
    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return when (type.classifier as? KClass<*>) {
            Email::class -> EmailScalar.INSTANCE
            Password::class -> PasswordScalar.INSTANCE
            NewPassword::class -> NewPasswordScalar.INSTANCE
            Date::class -> DateScalar.INSTANCE
            Long::class -> Scalars.GraphQLInt
            else -> null
        }
    }
}
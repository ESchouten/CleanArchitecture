package graphql

import com.expediagroup.graphql.generator.scalars.IDValueUnboxer
import domain.entity.ValueClass
import domain.entity.user.Email
import domain.entity.user.NewPassword
import domain.entity.user.Password
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.GraphQLScalarType

/**
 * detect value classes and return .value in graphql schema
 */
class CustomValueUnboxer : IDValueUnboxer() {
    override fun unbox(`object`: Any?): Any? {
        if (`object` is ValueClass<*>)
            return `object`.value
        return super.unbox(`object`)
    }
}

/**
 * Scalars for value classes
 * I think there's a better way to handle value classes scalars...
 */
object EmailScalar {
        private val coercing: Coercing<Email, String?> = object : Coercing<Email, String?> {
            override fun serialize(dataFetcherResult: Any): String {
                return dataFetcherResult.toString()
            }

            override fun parseValue(input: Any): Email {
                if (input is String)
                    return Email(input)
                throw CoercingParseValueException()
            }

            override fun parseLiteral(input: Any): Email {
                if (input is StringValue)
                    return Email(input.value)
                throw CoercingParseLiteralException()
            }
        }

    var INSTANCE: GraphQLScalarType? = GraphQLScalarType.newScalar()
        .name("Email")
        .coercing(coercing)
        .build()
}

object PasswordScalar {
    private val coercing: Coercing<Password, String?> = object : Coercing<Password, String?> {
        override fun serialize(dataFetcherResult: Any): String {
            return dataFetcherResult.toString()
        }

        override fun parseValue(input: Any): Password {
            if (input is String)
                return Password(input)
            throw CoercingParseValueException()
        }

        override fun parseLiteral(input: Any): Password {
            if (input is StringValue)
                return Password(input.value)
            throw CoercingParseLiteralException()
        }
    }

    var INSTANCE: GraphQLScalarType? = GraphQLScalarType.newScalar()
        .name("Password")
        .coercing(coercing)
        .build()
}

object NewPasswordScalar {
    private val coercing: Coercing<NewPassword, String?> = object : Coercing<NewPassword, String?> {
        override fun serialize(dataFetcherResult: Any): String {
            return dataFetcherResult.toString()
        }

        override fun parseValue(input: Any): NewPassword {
            if (input is String)
                return NewPassword(input)
            throw CoercingParseValueException()
        }

        override fun parseLiteral(input: Any): NewPassword {
            if (input is StringValue)
                return NewPassword(input.value)
            throw CoercingParseLiteralException()
        }
    }

    var INSTANCE: GraphQLScalarType? = GraphQLScalarType.newScalar()
        .name("NewPassword")
        .coercing(coercing)
        .build()
}
package com.erikschouten.cleanarchitecture.graphql

import com.apurebase.kgraphql.GraphQL
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import com.erikschouten.cleanarchitecture.auth.UserPrincipal
import io.ktor.auth.*
import com.erikschouten.cleanarchitecture.usecases.usecase.UsecaseType

fun GraphQL.Configuration.configure(usecases: Array<UsecaseType<*>>, development: Boolean = false) {
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

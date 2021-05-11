package com.erikschouten.cleanarchitecture

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.testing.*
import kotlin.test.Test

class GraphqlTests {

    @Test
    fun graphqlTest() {
        withApplication(createTestEnvironment { config = HoconApplicationConfig(ConfigFactory.load()) }, test = {

        })
    }
}

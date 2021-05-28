pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":domain",
    ":use-cases",
    ":adapters:authentication",
    ":adapters:config",
    ":adapters:graphql",
    ":adapters:repositories",
    ":adapters:server"
)

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("coroutines", "1.5.0")
            version("ktor", "1.6.0")
            version("logback", "1.2.3")
            version("koin", "3.0.2")
            version("kgraphql", "0.17.8")
            version("exposed", "0.31.1")
            version("jwt", "3.16.0")
            version("bcrypt", "0.9.0")
            version("hikari", "4.0.3")
            version("mariadb", "2.7.3")
            version("h2", "1.4.200")
            version("flyway", "7.9.1")
        }
    }
}


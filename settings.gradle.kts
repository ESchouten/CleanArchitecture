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
            version("coroutines", "1.5.2")
            version("ktor", "1.6.3")
            version("logback", "1.2.6")
            version("koin", "3.1.2")
            version("kgraphql", "0.17.14")
            version("exposed", "0.35.1")
            version("jwt", "3.18.1")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.0")
            version("mariadb", "2.7.4")
            version("h2", "1.4.200")
            version("flyway", "7.15.0")
            version("mockk", "1.12.0")
        }
    }
}


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
            version("coroutines", "1.5.1")
            version("ktor", "1.6.2")
            version("logback", "1.2.5")
            version("koin", "3.0.2")
            version("kgraphql", "0.17.13")
            version("exposed", "0.32.1")
            version("jwt", "3.18.1")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.0")
            version("mariadb", "2.7.3")
            version("h2", "1.4.200")
            version("flyway", "7.12.1")
            version("mockk", "1.12.0")
        }
    }
}


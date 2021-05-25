pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

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
            version("ktor", "1.5.4")
            version("logback", "1.2.3")
            version("koin", "3.0.2")
            version("kgraphql", "0.17.8")
            version("exposed", "0.31.1")
            version("jwt", "3.16.0")
            version("bcrypt", "0.9.0")
        }
    }
}


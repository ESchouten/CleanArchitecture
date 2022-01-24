pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":domain",
    ":use-cases",
    ":infrastructure:ktor",
    ":adapters:authentication",
    ":adapters:config",
    ":adapters:graphql",
    ":adapters:repositories"
)

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.6.10")
            version("shadowjar", "7.1.2")
            version("coroutines", "1.6.0")
            version("ktor", "1.6.7")
            version("logback", "1.3.0-alpha12")
            version("koin", "3.1.5")
            version("kgraphql", "0.17.14")
            version("exposed", "0.37.3")
            version("jwt", "3.18.3")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.1")
            version("mariadb", "2.7.4")
            version("h2", "1.4.200")
            version("flyway", "8.1.0")
            version("mockk", "1.12.2")
            version("updateversions", "0.41.0")
        }
    }
}


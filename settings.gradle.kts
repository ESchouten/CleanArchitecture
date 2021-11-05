pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":domain",
    ":use-cases",
    ":infrastructure",
    ":adapters:authentication",
    ":adapters:config",
    ":adapters:graphql",
    ":adapters:repositories"
)

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.6.0-RC2")
            version("shadowjar", "7.1.0")
            version("coroutines", "1.5.2")
            version("ktor", "1.6.5")
            version("logback", "1.3.0-alpha10")
            version("koin", "3.1.3")
            version("kgraphql", "0.17.14")
            version("exposed", "0.36.1")
            version("jwt", "3.18.2")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.0")
            version("mariadb", "2.7.4")
            version("h2", "1.4.200")
            version("flyway", "8.0.3")
            version("mockk", "1.12.0")
            version("updateversions", "0.39.0")
        }
    }
}


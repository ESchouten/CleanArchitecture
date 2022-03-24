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

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.6.20-RC2")
            version("shadowjar", "7.1.2")
            version("coroutines", "1.6.0")
            version("ktor", "1.6.8")
            version("logback", "1.3.0-alpha14")
            version("koin", "3.2.0-beta-1")
            version("kgraphql", "0.17.14")
            version("exposed", "0.37.3")
            version("jwt", "3.18.3")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.1")
            version("mariadb", "3.0.3")
            version("h2", "2.1.210")
            version("flyway", "8.5.4")
            version("mockk", "1.12.3")
            version("updateversions", "0.42.0")
        }
    }
}


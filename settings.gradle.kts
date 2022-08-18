rootProject.name = "CleanArchitecture"

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
    ":adapters:logging",
    ":adapters:repositories"
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.7.20-Beta")
            version("shadowjar", "7.1.2")
            version("coroutines", "1.6.4")
            version("ktor", "2.1.0")
            version("slf4j", "2.0.0-beta1")
            version("logback", "1.3.0-beta0")
            version("koin", "3.2.0")
            version("kgraphql", "0.17.15")
            version("exposed", "0.39.2")
            version("jwt", "4.0.0")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.1")
            version("mariadb", "3.0.7")
            version("h2", "2.1.214")
            version("flyway", "9.1.6")
            version("mockk", "1.12.5")
            version("reflections", "0.10.2")
            version("updateversions", "0.42.0")
        }
    }
}


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
            version("kotlin", "1.7.0")
            version("shadowjar", "7.1.2")
            version("coroutines", "1.6.3")
            version("ktor", "2.0.3")
            version("slf4j", "1.7.36")
            version("logback", "1.3.0-alpha16")
            version("koin", "3.2.0")
            version("kgraphql", "0.17.14")
            version("exposed", "0.38.2")
            version("jwt", "4.0.0")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.1")
            version("mariadb", "3.0.5")
            version("h2", "2.1.214")
            version("flyway", "8.5.13")
            version("mockk", "1.12.4")
            version("reflections", "0.10.2")
            version("updateversions", "0.42.0")
        }
    }
}


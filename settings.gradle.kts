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
            version("kotlin", "1.8.20")
            version("shadowjar", "8.1.1")
            version("coroutines", "1.7.0-Beta")
            version("ktor", "2.2.4")
            version("kompendium", "3.13.0")
            version("slf4j", "2.0.7")
            version("logback", "1.4.6")
            version("koin", "3.2.2")
            version("kgraphql", "0.19.0")
            version("exposed", "0.41.1")
            version("jwt", "4.4.0")
            version("bcrypt", "0.10.2")
            version("hikari", "5.0.1")
            version("mariadb", "3.1.3")
            version("h2", "2.1.214")
            version("flyway", "9.16.3")
            version("mockk", "1.13.4")
            version("reflections", "0.10.2")
            version("updateversions", "0.46.0")
        }
    }
}


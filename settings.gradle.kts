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
            version("kotlin", "1.7.21")
            version("shadowjar", "7.1.2")
            version("coroutines", "1.6.4")
            version("ktor", "2.1.3")
            version("kompendium", "3.9.0")
            version("slf4j", "2.0.3")
            version("logback", "1.4.4")
            version("koin", "3.2.2")
            version("kgraphql", "0.18.1")
            version("exposed", "0.41.1")
            version("jwt", "4.2.1")
            version("bcrypt", "0.9.0")
            version("hikari", "5.0.1")
            version("mariadb", "3.0.8")
            version("h2", "2.1.214")
            version("flyway", "9.8.1")
            version("mockk", "1.13.2")
            version("reflections", "0.10.2")
            version("updateversions", "0.44.0")
        }
    }
}


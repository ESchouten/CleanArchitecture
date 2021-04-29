pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    val kotlinVersion = "1.5.0"

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
    }
}

include(
    ":entities",
    ":use-cases",
    ":adapters"
)


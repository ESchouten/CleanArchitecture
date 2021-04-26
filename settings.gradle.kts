pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    val kotlinVersion = "1.5.0-RC"

    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}

include(
    ":entities",
    ":use-cases"
)


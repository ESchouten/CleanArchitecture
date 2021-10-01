@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
    id("com.github.ben-manes.versions") version libs.versions.updateversions
}

allprojects {
    group = "com.erikschouten"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "kotlin")
    repositories {
        mavenCentral()
    }

    afterEvaluate {
        dependencies {
            "testImplementation"(kotlin("test"))
            "testImplementation"("io.mockk", "mockk", libs.versions.mockk.get())
        }
    }
}

plugins {
    kotlin("jvm") version "1.5.21" apply false
}

allprojects {
    group = "com.erikschouten"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "kotlin")
    repositories {
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }

    afterEvaluate {
        dependencies {
            "testImplementation"(kotlin("test"))
            "testImplementation"("io.mockk", "mockk", libs.versions.mockk.get())
        }
    }
}

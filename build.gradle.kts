plugins {
    kotlin("jvm") version "1.5.31" apply false
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

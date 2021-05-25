plugins {
    kotlin("jvm") version "1.5.10" apply false
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

    dependencies {
        "testImplementation"(kotlin("test"))
        "testImplementation"("io.mockk:mockk:1.11.0")
    }
}

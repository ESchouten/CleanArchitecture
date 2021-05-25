plugins {
    kotlin("jvm") version "1.5.0" apply false
}

allprojects {
    group = "com.erikschouten"
    version = "1.0-SNAPSHOT"
}

subprojects {
    repositories {
        mavenCentral()
    }
    apply(plugin = "kotlin")

    dependencies {
        "testImplementation"(kotlin("test"))
        "testImplementation"("io.mockk:mockk:1.11.0")
    }
}

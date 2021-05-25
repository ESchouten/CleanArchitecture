plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":domain"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
    testImplementation("io.mockk:mockk:1.11.0")
}

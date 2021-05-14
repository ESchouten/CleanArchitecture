plugins {
    kotlin("jvm")
}

dependencies {
    val koinVersion = "3.0.1"

    implementation(project(":domain"))
    implementation(project(":use-cases"))
    api(project(":adapters:authentication"))
    implementation(project(":adapters:repositories"))

    api("io.insert-koin:koin-core:$koinVersion")
    api("io.insert-koin:koin-core-ext:$koinVersion")
}

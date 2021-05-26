dependencies {
    implementation(project(":domain"))

    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", libs.versions.coroutines.get())
    testImplementation(kotlin("reflect"))
}

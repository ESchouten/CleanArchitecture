dependencies {
    implementation(projects.domain)

    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", libs.versions.coroutines.get())
    testImplementation(kotlin("reflect", libs.versions.kotlin.get()))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi"
}

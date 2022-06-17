dependencies {
    implementation(projects.domain)
    implementation(projects.useCases)
    api(projects.adapters.authentication)
    implementation(projects.adapters.logging)
    implementation(projects.adapters.repositories)

    api("io.insert-koin", "koin-core", libs.versions.koin.get())
    implementation("org.reflections", "reflections", libs.versions.reflections.get())
    implementation(kotlin("reflect", libs.versions.kotlin.get()))

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            listOf(
                "-Xopt-in=org.koin.core.annotation.KoinInternalApi",
                "-Xopt-in=org.koin.core.annotation.KoinReflectAPI"
            )
    }
}

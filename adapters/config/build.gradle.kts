dependencies {
    implementation(projects.domain)
    implementation(projects.useCases)
    api(projects.adapters.authentication)
    implementation(projects.adapters.repositories)

    api("io.insert-koin", "koin-core", libs.versions.koin.get())
    implementation(kotlin("reflect"))
}

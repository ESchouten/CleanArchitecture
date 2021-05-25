dependencies {
    implementation(project(":domain"))
    implementation(project(":use-cases"))
    api(project(":adapters:authentication"))
    implementation(project(":adapters:repositories"))

    api("io.insert-koin", "koin-core", libs.versions.koin.get())
    api("io.insert-koin", "koin-core-ext", libs.versions.koin.get())
}

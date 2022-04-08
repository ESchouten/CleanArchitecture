dependencies {
    implementation(projects.useCases)
    implementation(projects.domain)

    implementation("com.apurebase", "kgraphql", libs.versions.kgraphql.get())

    implementation(kotlin("reflect", libs.versions.kotlin.get()))
}

dependencies {
    implementation(projects.useCases)

    implementation("com.apurebase", "kgraphql", libs.versions.kgraphql.get())

    implementation(kotlin("reflect"))
}

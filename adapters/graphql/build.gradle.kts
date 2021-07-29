dependencies {
    implementation(projects.useCases)
    implementation(projects.domain)

//    implementation("com.apurebase", "kgraphql", libs.versions.kgraphql.get())
    implementation("com.github.ESchouten", "kgraphql", "dd8234386c")

    implementation(kotlin("reflect"))
}

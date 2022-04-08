dependencies {
    implementation(projects.useCases)
    implementation(projects.domain)

    // Custom KGraphql build for Ktor 2.0 support
    implementation("com.github.ESchouten.KGraphQL", "kgraphql", libs.versions.kgraphql.get())

    implementation(kotlin("reflect", libs.versions.kotlin.get()))
}

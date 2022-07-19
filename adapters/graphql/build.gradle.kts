dependencies {
    implementation(projects.useCases)
    implementation(projects.domain)

    implementation("com.expediagroup", "graphql-kotlin-server", libs.versions.graphqlkotlin.get())
    implementation("com.graphql-java", "graphql-java-extended-scalars", libs.versions.graphqlScalars.get())

    implementation(kotlin("reflect", libs.versions.kotlin.get()))
}

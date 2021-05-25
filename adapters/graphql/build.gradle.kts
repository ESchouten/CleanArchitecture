dependencies {
    implementation(project(":use-cases"))

    implementation("com.apurebase", "kgraphql", libs.versions.kgraphql.get())

    implementation(kotlin("reflect"))
}

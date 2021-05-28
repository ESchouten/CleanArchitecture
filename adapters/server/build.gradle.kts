plugins {
    application
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.useCases)
    implementation(projects.adapters.config)
    implementation(projects.adapters.graphql)

    implementation("io.ktor", "ktor-server-core", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-server-cio", libs.versions.ktor.get())
    implementation("io.ktor","ktor-auth", libs.versions.ktor.get())
    implementation("io.ktor", "ktor-auth-jwt", libs.versions.ktor.get())
    implementation("ch.qos.logback", "logback-classic", libs.versions.logback.get())
    implementation("io.insert-koin", "koin-ktor", libs.versions.koin.get())
    implementation("com.apurebase", "kgraphql-ktor", libs.versions.kgraphql.get())
}

plugins {
    application
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    val ktorVersion = "1.5.4"

    implementation(project(":domain"))
    implementation(project(":use-cases"))
    implementation(project(":adapters:config"))
    implementation(project(":adapters:graphql"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.insert-koin:koin-ktor:3.0.1")
    implementation("com.apurebase:kgraphql-ktor:0.17.8")
}

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    val ktorVersion = "1.5.4"
    
    implementation(project(":use-cases"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.insert-koin:koin-ktor:3.0.1")
    implementation("com.apurebase:kgraphql-ktor:0.17.8")

    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
}

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    val ktorVersion = "1.5.4"
    val koinVersion = "3.0.1"

    implementation(project(":use-cases"))
    implementation(project(":adapters:repositories"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-core-ext:$koinVersion")
    implementation("com.apurebase:kgraphql-ktor:0.17.8")
    implementation("at.favre.lib:bcrypt:0.9.0")

    implementation(kotlin("reflect"))
}

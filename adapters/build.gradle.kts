plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

dependencies {
    implementation(project(":use-cases"))
    implementation("io.ktor:ktor-server-core:1.5.4")
    implementation("io.ktor:ktor-server-cio:1.5.4")
    implementation("io.ktor:ktor-auth:1.5.4")
    implementation("io.ktor:ktor-auth-jwt:1.5.4")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.insert-koin:koin-ktor:3.0.1")
    implementation("com.apurebase:kgraphql-ktor:0.17.8")
//    implementation("com.github.ESchouten.kgraphql:kgraphql-ktor:10e1e35daa")

    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
}

repositories {
    maven("https://jitpack.io")
}

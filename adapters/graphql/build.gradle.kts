plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":use-cases"))

    implementation("com.apurebase:kgraphql:0.17.8")

    implementation(kotlin("reflect"))
}

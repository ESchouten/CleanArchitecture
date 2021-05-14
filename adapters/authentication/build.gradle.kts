plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":use-cases"))

    implementation("com.auth0:java-jwt:3.16.0")
    implementation("at.favre.lib:bcrypt:0.9.0")
}

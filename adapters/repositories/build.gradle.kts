dependencies {
    implementation(project(":domain"))

    implementation("org.jetbrains.exposed", "exposed-dao", libs.versions.exposed.get())
    implementation("org.jetbrains.exposed", "exposed-jdbc", libs.versions.exposed.get())
}

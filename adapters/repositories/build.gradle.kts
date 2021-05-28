dependencies {
    implementation(project(":domain"))

    implementation("org.jetbrains.exposed", "exposed-dao", libs.versions.exposed.get())
    implementation("org.jetbrains.exposed", "exposed-jdbc", libs.versions.exposed.get())
    implementation("com.zaxxer", "HikariCP", libs.versions.hikari.get())
    implementation("org.mariadb.jdbc", "mariadb-java-client", libs.versions.mariadb.get())
    implementation("com.h2database", "h2", libs.versions.h2.get())
    implementation("org.flywaydb", "flyway-core", libs.versions.flyway.get())

    testImplementation(kotlin("reflect"))
}

dependencies {
    implementation(projects.useCases)

    implementation("org.slf4j", "slf4j-api", libs.versions.slf4j.get())
    implementation("ch.qos.logback", "logback-core", libs.versions.logback.get())
}

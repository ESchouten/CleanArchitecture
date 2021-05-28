dependencies {
    implementation(projects.domain)
    implementation(projects.useCases)

    implementation("com.auth0", "java-jwt", libs.versions.jwt.get())
    implementation("at.favre.lib", "bcrypt", libs.versions.bcrypt.get())
}

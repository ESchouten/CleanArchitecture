plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        commonMain{
            dependencies {
                api(project(":entities"))
            }
        }
    }
}

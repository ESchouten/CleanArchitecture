plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm().compilations.all {
        kotlinOptions {
            useIR = true
        }
    }

    sourceSets {
        commonMain{
            dependencies {
                api(project(":entities"))
            }
        }
    }
}

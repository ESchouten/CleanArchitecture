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
                api("com.benasher44:uuid:0.2.3")
            }
        }
    }
}

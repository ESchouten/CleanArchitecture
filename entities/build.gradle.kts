plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.benasher44:uuid:0.2.3")
            }
        }
    }
}

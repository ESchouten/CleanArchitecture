plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        commonMain{
            dependencies {
                api("com.benasher44:uuid:0.2.3")
            }
        }
    }
}

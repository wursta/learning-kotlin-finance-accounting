plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":fm-common"))
                implementation(project(":fm-stubs"))
            }
        }
    }
}
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {}

    sourceSets {
        val kermitLoggerVersion: String by project
        val coroutinesVersion: String by project

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("co.touchlab:kermit:$kermitLoggerVersion")

                // PROJECT
                implementation(project(":fm-common"))
            }
        }
    }
}
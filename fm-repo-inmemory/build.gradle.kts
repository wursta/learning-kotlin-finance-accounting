plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        val cache4kVersion: String by project
        val uuidVersion: String by project
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
                implementation("com.benasher44:uuid:$uuidVersion")


                // Project
                implementation(project(":fm-common"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
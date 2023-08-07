plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                val crowdprojKotlinCorVersion: String by project
                implementation(kotlin("stdlib-common"))
                implementation("com.crowdproj:kotlin-cor:$crowdprojKotlinCorVersion")

                implementation(project(":fm-common"))
                implementation(project(":fm-stubs"))
                implementation(project(":fm-repo-inmemory"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                val coroutinesVersion: String by project
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
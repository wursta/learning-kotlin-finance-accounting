plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        val datetimeVersion: String by project

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
    }
}
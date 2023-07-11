import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

allprojects {
    group = "local.learning"
    version = "latest"

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    tasks.withType<KotlinCompile>{
        kotlinOptions.jvmTarget = "17"
    }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = "local.kotlin.learning"
    version = "1.0-SNAPSHOT"

    tasks.withType<KotlinCompile>{
        kotlinOptions.jvmTarget = "17"
    }
}
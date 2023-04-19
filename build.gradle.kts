import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "local.learning"
version = "1.0-SNAPSHOT"


allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    this.group = group
    this.version = version

    tasks.withType<KotlinCompile>{
        kotlinOptions.jvmTarget = "17"
    }
}
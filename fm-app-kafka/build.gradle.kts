plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("local.learning.app.kafka.ApplicationKt")
}

dependencies {
    // Libs
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val log4jVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")
    implementation("org.slf4j:slf4j-log4j12:$log4jVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    // Project
    implementation(project(":fm-common"))
    implementation(project(":fm-api"))
    implementation(project(":fm-mappers"))
    implementation(project(":fm-biz"))

    // Tests
    testImplementation(kotlin("test-junit"))
}

tasks.register("printEnvironment") {
    doLast {
        System.getenv().forEach { k, v ->
            println("$k -> $v")
        }
    }
}
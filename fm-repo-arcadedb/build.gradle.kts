plugins {
    kotlin("jvm")
}

dependencies {
    val arcadeDbVersion: String by project
    val coroutinesVersion: String by project
    val testcontainersVersion: String by project
    val uuidVersion: String by project

    implementation("com.benasher44:uuid:$uuidVersion")
    implementation("com.arcadedb:arcadedb-engine:$arcadeDbVersion")
    implementation("com.arcadedb:arcadedb-network:$arcadeDbVersion")

    // Project
    implementation(project(":fm-common"))

    // Test
    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
}
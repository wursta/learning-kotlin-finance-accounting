plugins {
    kotlin("jvm")
}

dependencies {
    // Libs
    val ktorClientOkhttpVersion: String by project
    val log4jVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    implementation("io.ktor:ktor-client-okhttp-jvm:$ktorClientOkhttpVersion")
    implementation("org.slf4j:slf4j-log4j12:$log4jVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    // Project
    implementation(project(":fm-api"))
    implementation(project(":fm-repo-arcadedb"))


    // Tests
    val kotestVersion: String by project
    val testcontainersVersion: String by project
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn(":fm-app-ktor:publishImageToLocalRegistry")
    }
    test {
        systemProperty("kotest.framework.test.severity", "NORMAL")
    }
    create<Test>("test-strict") {
        systemProperty("kotest.framework.test.severity", "MINOR")
        group = "verification"
    }
}
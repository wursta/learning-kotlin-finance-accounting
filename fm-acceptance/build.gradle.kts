plugins {
    kotlin("jvm")
}

dependencies {
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
    }
    test {
        systemProperty("kotest.framework.test.severity", "NORMAL")
    }
    create<Test>("test-strict") {
        systemProperty("kotest.framework.test.severity", "MINOR")
        group = "verification"
    }
}
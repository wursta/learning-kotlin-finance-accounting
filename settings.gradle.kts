
rootProject.name = "finance-accounting"
include("m1l1-helloworld")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}

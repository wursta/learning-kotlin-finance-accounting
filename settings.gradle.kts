
rootProject.name = "finance-accounting"
include("m1l1-helloworld")
include("m1l3-oop")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}

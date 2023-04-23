
rootProject.name = "finance-management"
//include("m1l1-helloworld")
//include("m1l3-oop")
//include("m1l4-dsl")
include("fm-acceptance")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}

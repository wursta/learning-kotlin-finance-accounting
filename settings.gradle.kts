
rootProject.name = "finance-management"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val ktorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
    }
}

include("fm-acceptance")
include("fm-api")
include("fm-common")
include("fm-mappers")
include("fm-biz")
include("fm-stubs")
include("fm-app-ktor")
include("fm-app-kafka")

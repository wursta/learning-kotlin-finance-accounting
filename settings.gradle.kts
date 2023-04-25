
rootProject.name = "finance-management"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("fm-acceptance")
include("fm-api-v1")
include("fm-common")
include("fm-mappers")

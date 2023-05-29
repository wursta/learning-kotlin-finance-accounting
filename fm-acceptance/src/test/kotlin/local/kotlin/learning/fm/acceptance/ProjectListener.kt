package local.kotlin.learning.fm.acceptance

import io.kotest.core.annotation.AutoScan
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import mu.KotlinLogging

private val log = KotlinLogging.logger {}
@Suppress("unused")
@AutoScan
class ProjectListener : BeforeProjectListener, AfterProjectListener {
    override suspend fun beforeProject() {
        val host = AppCompose.C.hostApp
        val port = AppCompose.C.portApp
        log.info { "Started docker-compose with App at HOST: $host PORT: $port" }
    }

    override suspend fun afterProject() {
        AppCompose.C.close()
    }
}

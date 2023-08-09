package local.kotlin.learning.fm.acceptance

import io.kotest.core.annotation.AutoScan
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import local.learning.repo.arcadedb.ArcadeDbSchema
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

@Suppress("unused")
@AutoScan
class ProjectListener : BeforeProjectListener, AfterProjectListener {
    private val arcadeDbDbName = "FinancialManagement"
    private val arcadeDbUserName = "root"
    private val arcadeDbUserPass = "root_root"
    override suspend fun beforeProject() {
        val appHost = AppCompose.C.hostApp
        val appPort = AppCompose.C.portApp
        val arcadeDbHost = AppCompose.C.hostArcadeDb
        val arcadeDbPort = AppCompose.C.portArcadeDb

        log.info { "Started docker-compose with App at HOST: $appHost PORT: $appPort and ArcadeDB at HOST: $arcadeDbHost PORT: $arcadeDbPort" }

        ArcadeDbSchema.initialize(arcadeDbHost, arcadeDbPort, arcadeDbDbName, arcadeDbUserName, arcadeDbUserPass)
    }

    override suspend fun afterProject() {
        AppCompose.C.close()
    }
}

package local.kotlin.learning.fm.acceptance

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

@Suppress("unused")
class AppCompose private constructor() {
    private val _app_service = "app_1"
    private val _app_port = 8080

    private val _arcadedb_service = "arcadedb_1"
    private val _arcadedb_port = 2480

    private val compose =
        DockerComposeContainer(File("../deploy/docker-compose.yml")).apply {
            withExposedService(
                _app_service,
                _app_port
            )
            withExposedService(
                _arcadedb_service,
                _arcadedb_port
            )
            withLocalCompose(true)
            waitingFor("arcadedb", Wait.forLogMessage(".*ArcadeDB Server started.*\\n", 1))
            withStartupTimeout(Duration.ofMinutes(5))
            start()
        }

    val hostApp: String = compose.getServiceHost(_app_service, _app_port)
    val portApp: Int = compose.getServicePort(_app_service, _app_port)

    val hostArcadeDb: String = compose.getServiceHost(_arcadedb_service, _arcadedb_port)
    val portArcadeDb: Int = compose.getServicePort(_arcadedb_service, _arcadedb_port)

    fun close() {
        compose.close()
    }
    companion object {
        val C by lazy { AppCompose() }
        val arcadeDbName = "FinancialManagement"
        val arcadeDbUserName = "root"
        val arcadeDbUserPass = "root_root"
    }
}

package local.kotlin.learning.fm.acceptance

import org.testcontainers.containers.DockerComposeContainer
import java.io.File

@Suppress("unused")
class AppCompose private constructor() {
    private val _service = "app_1"
    private val _port = 80

    private val compose =
        DockerComposeContainer(File("../deploy/docker-compose.yml")).apply {
            withExposedService(
                _service,
                _port
            )
            withLocalCompose(true)
            start()
        }

    val hostApp: String = compose.getServiceHost(_service, _port)
    val portApp: Int = compose.getServicePort(_service, _port)

    fun close() {
        compose.close()
    }

    companion object {
        val C by lazy { AppCompose() }
    }
}

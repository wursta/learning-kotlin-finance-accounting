package local.learning.app.ktor

import io.ktor.server.application.*
import local.learning.common.log.LoggerProvider
import local.learning.logging.kermit.getKermitLoggerWrapper

actual fun Application.getLoggerProviderConf(): LoggerProvider = LoggerProvider {
    getKermitLoggerWrapper(it)
}
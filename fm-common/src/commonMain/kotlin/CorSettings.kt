package local.learning.common

import local.learning.common.log.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
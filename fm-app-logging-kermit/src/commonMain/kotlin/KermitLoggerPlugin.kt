package local.learning.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import local.learning.common.log.ILogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun getKermitLoggerWrapper(cls: KClass<*>): ILogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return KermitLoggerWrapper(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}
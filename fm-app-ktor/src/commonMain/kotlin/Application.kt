package local.learning.app.ktor

import CardArcadeDbRepository
import ExpenseArcadeDbRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import local.learning.api.models.InternalErrorResponseDto
import local.learning.api.serialization.utils.jsonSerializer
import local.learning.app.biz.CardProcessor
import local.learning.app.biz.ExpenseProcessor
import local.learning.app.ktor.routing.card
import local.learning.app.ktor.routing.expense
import local.learning.common.CorSettings
import local.learning.common.log.LoggerProvider
import local.learning.repo.inmemory.CardInMemoryRepository
import local.learning.repo.inmemory.ExpenseInMemoryRepository

expect fun Application.getLoggerProviderConf(): LoggerProvider
fun Application.initAppSettings(): ApplicationSettings {
    val arcadeDbHost = environment.config.property("arcadeDb.host").getString()
    val arcadeDbPort = environment.config.property("arcadeDb.port").getString().toInt()
    val arcadeDbName = environment.config.property("arcadeDb.dbName").getString()
    val arcadeDbUserName = environment.config.property("arcadeDb.user").getString()
    val arcadeDbUserPass = environment.config.property("arcadeDb.pass").getString()

    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
        cardRepoTest = CardInMemoryRepository(),
        cardRepoProd = CardArcadeDbRepository(
            host = arcadeDbHost,
            port = arcadeDbPort,
            dbName = arcadeDbName,
            userName = arcadeDbUserName,
            userPassword = arcadeDbUserPass
        ),
        expenseRepoTest = ExpenseInMemoryRepository(),
        expenseRepoProd = ExpenseArcadeDbRepository(
            host = arcadeDbHost,
            port = arcadeDbPort,
            dbName = arcadeDbName,
            userName = arcadeDbUserName,
            userPassword = arcadeDbUserPass
        )
    )

    return ApplicationSettings(
        corSettings = corSettings,
        cardProcessor = CardProcessor(corSettings),
        expenseProcessor = ExpenseProcessor(corSettings),
    )
}

fun Application.mainModule(
    appSettings: ApplicationSettings = initAppSettings()
) {
    val swaggerHost = environment.config.propertyOrNull("ktor.swaggerHost")?.getString()
    if (swaggerHost != null) {
        install(CORS) {
            allowHost(swaggerHost)
            allowHeader(HttpHeaders.ContentType)
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            var message = cause.message
            if (call.application.environment.developmentMode) {
                message += " Trace: ${cause.stackTraceToString()}"
            }
            call.respondText(
                text = jsonSerializer.encodeToString(
                    InternalErrorResponseDto(
                        message = cause.message
                    )
                ),
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    routing {
        route("api") {
            install(ContentNegotiation) {
                json(jsonSerializer)
            }

            card(appSettings)
            expense(appSettings)
        }
    }
}

fun main() {
    embeddedServer(CIO) {
        mainModule()
    }.start(wait = true)
}
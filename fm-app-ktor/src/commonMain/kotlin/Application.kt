package local.learning.app.ktor

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import local.learning.api.models.InternalErrorResponseDto
import local.learning.api.serialization.utils.jsonSerializer
import local.learning.app.biz.CardProcessor
import local.learning.app.biz.ExpenseProcessor
import local.learning.app.ktor.routing.card
import local.learning.app.ktor.routing.expense
import local.learning.common.CorSettings
import local.learning.common.log.LoggerProvider
import local.learning.repo.arcadedb.ArcadeDbSettings
import local.learning.repo.arcadedb.CardArcadeDbRepository
import local.learning.repo.arcadedb.ExpenseArcadeDbRepository
import local.learning.repo.inmemory.CardInMemoryRepository
import local.learning.repo.inmemory.ExpenseInMemoryRepository

expect fun Application.getLoggerProviderConf(): LoggerProvider
fun Application.initAppSettings(): ApplicationSettings {
    val arcadeDbSettings = ArcadeDbSettings(
        host = environment.config.property("arcadeDb.host").getString(),
        port = environment.config.property("arcadeDb.port").getString().toInt(),
        dbName = environment.config.property("arcadeDb.dbName").getString(),
        userName = environment.config.property("arcadeDb.user").getString(),
        userPassword = environment.config.property("arcadeDb.pass").getString()
    )

    val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }

    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
        principalRepo = PrincipalInMemoryRepository(digestFunction),
        cardRepoTest = CardInMemoryRepository(),
        cardRepoProd = CardArcadeDbRepository(arcadeDbSettings),
        expenseRepoTest = ExpenseInMemoryRepository(),
        expenseRepoProd = ExpenseArcadeDbRepository(arcadeDbSettings)
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

    install(Authentication) {
        basic("main-auth") {
            realm = "Access to the app"
            validate { credentials ->
                val principal = appSettings.corSettings.principalRepo.authenticate(credentials.name, credentials.password)
                if (principal != null) {
                    UserIdPrincipal(principal.id.asString())
                } else {
                    null
                }
            }
        }
    }

    routing {
        authenticate("main-auth") {
            route("api") {
                install(ContentNegotiation) {
                    json(jsonSerializer)
                }

                card(appSettings)
                expense(appSettings)
            }
        }
    }
}

fun main() {
    embeddedServer(CIO) {
        mainModule()
    }.start(wait = true)
}
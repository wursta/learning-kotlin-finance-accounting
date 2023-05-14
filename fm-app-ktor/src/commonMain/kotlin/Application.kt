package local.learning.app.ktor

//import io.ktor.server.plugins.swagger.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
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

fun Application.mainModule(
    cardProcessor: CardProcessor = CardProcessor(),
    expenseProcessor: ExpenseProcessor = ExpenseProcessor()
) {
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
//        static("swagger") {
//            files("resources/specs")
//        }
//        swaggerUI(path = "swagger", swaggerFile = "resources/specs/openapi.yaml")


        route("api") {
            install(ContentNegotiation) {
                json(jsonSerializer)
            }

            card(cardProcessor)
            expense(expenseProcessor)
        }
    }
}

fun main() {
    embeddedServer(CIO) {
        mainModule()
    }.start(wait = true)
}
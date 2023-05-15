package local.learning.app.ktor.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import local.learning.api.models.CardCreateRequestDto
import local.learning.api.models.CardDeleteRequestDto
import local.learning.api.models.CardReadRequestDto
import local.learning.api.models.CardUpdateRequestDto
import local.learning.app.biz.CardProcessor
import local.learning.app.ktor.controller.cardAction

fun Route.card(processor: CardProcessor) {
    route("card") {
        post("create") {
            call.cardAction<CardCreateRequestDto>(processor)
        }
        post("read") {
            call.cardAction<CardReadRequestDto>(processor)
        }
        post("update") {
            call.cardAction<CardUpdateRequestDto>(processor)
        }
        post("delete") {
            call.cardAction<CardDeleteRequestDto>(processor)
        }
    }
}
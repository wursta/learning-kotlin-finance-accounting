package local.learning.app.ktor.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import local.learning.api.models.CardCreateRequestDto
import local.learning.api.models.CardDeleteRequestDto
import local.learning.api.models.CardReadRequestDto
import local.learning.api.models.CardUpdateRequestDto
import local.learning.app.ktor.ApplicationSettings
import local.learning.app.ktor.controller.cardAction

fun Route.card(appSettings: ApplicationSettings) {
    route("card") {
        post("create") {
            call.cardAction<CardCreateRequestDto>(appSettings)
        }
        post("read") {
            call.cardAction<CardReadRequestDto>(appSettings)
        }
        post("update") {
            call.cardAction<CardUpdateRequestDto>(appSettings)
        }
        post("delete") {
            call.cardAction<CardDeleteRequestDto>(appSettings)
        }
    }
}
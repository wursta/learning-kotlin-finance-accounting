package local.learning.app.ktor.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import local.learning.api.models.IRequestDto
import local.learning.api.serialization.utils.jsonSerializer
import local.learning.app.ktor.ApplicationSettings
import local.learning.common.models.PrincipalId
import local.learning.common.models.card.CardCommand
import local.learning.mappers.fromTransport
import local.learning.mappers.toTransport

suspend inline fun <reified T : IRequestDto> ApplicationCall.cardAction(
    appSettings: ApplicationSettings,
    realCommand: CardCommand
) {
    appSettings.cardProcessor.process(
        logger = appSettings.corSettings.loggerProvider.logger(T::class),
        logId = T::class.qualifiedName ?: "unknown",
        command = realCommand,
        {
            val request = jsonSerializer.decodeFromString<T>(receiveText())

            val principalId = this@cardAction.principal<UserIdPrincipal>()?.name
            if (principalId != null) {
                val principal = appSettings.corSettings.principalRepo.getById(PrincipalId(principalId))
                if (principal != null) {
                    it.principal = principal
                }
            }
            it.fromTransport(request)
        },
        {
            respondText(jsonSerializer.encodeToString(it.toTransport()), ContentType.Application.Json)
        }
    )
}
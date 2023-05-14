package local.learning.app.ktor.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import local.learning.api.models.IRequestDto
import local.learning.api.models.InternalErrorResponseDto
import local.learning.api.serialization.utils.jsonSerializer
import local.learning.app.biz.ExpenseProcessor
import local.learning.common.ExpenseContext
import local.learning.mappers.fromTransport
import local.learning.mappers.toTransport

suspend inline fun <reified T : IRequestDto> ApplicationCall.expenseAction(processor: ExpenseProcessor) {
    try {
        val request = jsonSerializer.decodeFromString<T>(receiveText())
        val context = ExpenseContext()
        context.fromTransport(request)
        processor.exec(context)
        respondText(jsonSerializer.encodeToString(context.toTransport()), ContentType.Application.Json)
    } catch (e: Throwable) {
        respondText(
            text = jsonSerializer.encodeToString(
                InternalErrorResponseDto(
                    message = e.message
                )
            ),
            contentType = ContentType.Application.Json,
            status = HttpStatusCode.InternalServerError
        )
    }
}
package local.learning.mappers.v1

import local.learning.api.v1.models.*
import local.learning.common.models.Error
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardContext
import local.learning.common.models.card.CardGuid
import local.learning.mappers.v1.exceptions.UnknownCardCommand

fun CardContext.toTransport(): IResponseDto = when(val cmd = command) {
    CardCommand.CREATE -> toTransportCreate()
    CardCommand.READ -> toTransportRead()
    CardCommand.UPDATE -> toTransportUpdate()
    CardCommand.DELETE -> toTransportDelete()
    CardCommand.NONE -> throw  UnknownCardCommand(cmd)
}

fun CardContext.toTransportCreate() = CardCreateResponseDto(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

fun CardContext.toTransportRead() = CardReadResponseDto(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

fun CardContext.toTransportUpdate() = CardUpdateResponseDto(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

fun CardContext.toTransportDelete() = CardDeleteResponseDto(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

private fun Card.toTransport(): CardObjectDto = CardObjectDto(
    guid = guid.takeIf { it != CardGuid.NONE }?.asString(),
    number = number.takeIf { it.isNotBlank() },
    validFor = validFor.takeIf { it.isNotBlank() },
    owner = owner.takeIf { it.isNotBlank() }
)

private fun List<Error>.toTransportErrors(): List<ResponseErrorDto>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Error.toTransport() = ResponseErrorDto(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
package local.learning.mappers

import local.learning.api.models.*
import local.learning.common.CardContext
import local.learning.common.models.Error
import local.learning.common.models.LockGuid
import local.learning.common.models.State
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardGuid
import local.learning.mappers.exceptions.UnknownCardCommand

fun CardContext.toTransport(): IResponseDto = when(val cmd = command) {
    CardCommand.CREATE -> toTransportCreate()
    CardCommand.READ -> toTransportRead()
    CardCommand.UPDATE -> toTransportUpdate()
    CardCommand.DELETE -> toTransportDelete()
    CardCommand.NONE -> throw  UnknownCardCommand(cmd)
}

fun CardContext.toTransportCreate() = CardCreateResponseDto(
    responseType = "cardCreate",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FAILING) ResponseResultDto.ERROR else ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

fun CardContext.toTransportRead() = CardReadResponseDto(
    responseType = "cardRead",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FAILING) ResponseResultDto.ERROR else ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

fun CardContext.toTransportUpdate() = CardUpdateResponseDto(
    responseType = "cardUpdate",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FAILING) ResponseResultDto.ERROR else ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

fun CardContext.toTransportDelete() = CardDeleteResponseDto(
    responseType = "cardDelete",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FAILING) ResponseResultDto.ERROR else ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    card = cardResponse.toTransport()
)

private fun Card.toTransport(): CardObjectDto = CardObjectDto(
    guid = guid.takeIf { it != CardGuid.NONE }?.asString(),
    number = number.takeIf { it.isNotBlank() },
    validFor = validFor.takeIf { it.isNotBlank() },
    owner = owner.takeIf { it.isNotBlank() },
    bank = BankObjectDto(
        guid = bankGuid.asString()
    ),
    lock = lockGuid.takeIf { it != LockGuid.NONE }?.asString()
)

private fun List<Error>.toTransportErrors(): List<ResponseErrorDto> = this
    .map { it.toTransport() }
    .toList()

private fun Error.toTransport() = ResponseErrorDto(
    code = code.toString().lowercase(),
    group = group.toString().lowercase(),
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
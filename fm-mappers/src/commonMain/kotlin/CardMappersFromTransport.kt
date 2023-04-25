package local.learning.mappers.v1

import local.learning.api.v1.models.*
import local.learning.common.helpers.isNumberValid
import local.learning.common.helpers.isValidForValid
import local.learning.common.models.RequestId
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardContext
import local.learning.common.models.card.CardGuid
import local.learning.mappers.v1.exceptions.InvalidFieldFormat
import local.learning.mappers.v1.exceptions.UnknownRequestClass

private fun IRequestDto?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE
private fun String?.toCardGuid() = this?.let { CardGuid(it) } ?: CardGuid.NONE
private fun String?.toCardWithGuId() = Card(guid = this.toCardGuid())
@Suppress("unused")
fun CardContext.fromTransport(request: IRequestDto) = when (request) {
    is CardCreateRequestDto -> fromTransport(request)
    is CardReadRequestDto -> fromTransport(request)
    is CardUpdateRequestDto -> fromTransport(request)
    is CardDeleteRequestDto -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

fun CardContext.fromTransport(request: CardCreateRequestDto) {
    requestId = request.requestId()
    command = CardCommand.CREATE
    cardRequest = request.card?.toInternal() ?: Card()
}

fun CardContext.fromTransport(request: CardReadRequestDto) {
    requestId = request.requestId()
    command = CardCommand.READ
    cardRequest = request.guid.toCardWithGuId()
}

fun CardContext.fromTransport(request: CardUpdateRequestDto) {
    requestId = request.requestId()
    command = CardCommand.UPDATE
    cardRequest = request.card?.toInternal() ?: Card()
}

fun CardContext.fromTransport(request: CardDeleteRequestDto) {
    requestId = request.requestId()
    command = CardCommand.DELETE
    cardRequest = request.guid.toCardWithGuId()
}

private fun CardCreateObjectDto.toInternal(): Card {
    return Card(
        number = this.number ?: "",
        validFor = this.validFor ?: "",
        owner = this.owner ?: ""
    ).also {
        if (it.isNumberValid()) {
            throw InvalidFieldFormat("number", "0000-0000-0000-0000")
        }
        if (it.isValidForValid()) {
            throw InvalidFieldFormat("valid_for", "Y-m")
        }
    }
}

private fun CardObjectDto.toInternal(): Card {
    return Card(
        guid = CardGuid(this.guid ?: ""),
        number = this.number ?: "",
        validFor = this.validFor ?: "",
        owner = this.owner ?: ""
    ).also {
        if (it.isNumberValid()) {
            throw InvalidFieldFormat("number", "0000-0000-0000-0000")
        }
        if (it.isValidForValid()) {
            throw InvalidFieldFormat("valid_for", "Y-m")
        }
    }
}
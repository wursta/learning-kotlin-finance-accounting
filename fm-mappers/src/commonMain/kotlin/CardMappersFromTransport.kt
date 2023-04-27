package local.learning.mappers

import local.learning.api.models.*
import local.learning.common.CardContext
import local.learning.common.helpers.*
import local.learning.common.models.RequestId
import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardGuid
import local.learning.mappers.exceptions.InvalidFieldFormat
import local.learning.mappers.exceptions.UnknownRequestClass

private fun IRequestDto?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE

private fun String?.toBankGuid() = this?.let { BankGuid(it) } ?: BankGuid.NONE
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
        owner = this.owner ?: "",
        bankGuid = this.bank.toBankGuid()
    ).also {
        if (!it.isNumberValid()) {
            throw InvalidFieldFormat("number", "0000000000000000")
        }
        if (!it.isValidForValid()) {
            throw InvalidFieldFormat("valid_for", "Y-m")
        }
        if (!it.isOwnerValid()) {
            throw InvalidFieldFormat("owner", "String: from 3 to 50 chars")
        }
        if (!it.isBankGuidValid()) {
            throw InvalidFieldFormat("bank", "1598044e-5259-11e9-8647-d663bd873d93")
        }
    }
}

private fun CardObjectDto.toInternal(): Card {
    return Card(
        guid = CardGuid(this.guid ?: ""),
        number = this.number ?: "",
        validFor = this.validFor ?: "",
        owner = this.owner ?: "",
        bankGuid = BankGuid(this.bank?.guid ?: "")
    ).also {
        if (!it.isGuidValid()) {
            throw InvalidFieldFormat("guid", "1598044e-5259-11e9-8647-d663bd873d93")
        }
        if (!it.isNumberValid()) {
            throw InvalidFieldFormat("number", "0000000000000000")
        }
        if (!it.isValidForValid()) {
            throw InvalidFieldFormat("valid_for", "Y-m")
        }
        if (!it.isOwnerValid()) {
            throw InvalidFieldFormat("owner", "String: from 3 to 50 chars")
        }
        if (!it.isBankGuidValid()) {
            throw InvalidFieldFormat("bank", "1598044e-5259-11e9-8647-d663bd873d93")
        }
    }
}
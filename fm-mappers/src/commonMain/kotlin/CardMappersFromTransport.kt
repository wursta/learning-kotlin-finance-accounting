package local.learning.mappers

import local.learning.api.models.*
import local.learning.common.CardContext
import local.learning.common.models.LockGuid
import local.learning.common.models.RequestId
import local.learning.common.models.WorkMode
import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardGuid
import local.learning.common.models.card.CardStubCase
import local.learning.mappers.exceptions.UnknownRequestClass

private fun IRequestDto?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE

private fun String?.toBankGuid() = this?.let { BankGuid(it) } ?: BankGuid.NONE

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
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: CardStubCase.NONE
    command = CardCommand.CREATE
    cardRequest = request.card?.toInternal() ?: Card()
}

fun CardContext.fromTransport(request: CardReadRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: CardStubCase.NONE
    command = CardCommand.READ
    cardRequest = Card(guid = request.guid?.let { CardGuid(it) } ?: CardGuid.NONE)
}

fun CardContext.fromTransport(request: CardUpdateRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: CardStubCase.NONE
    command = CardCommand.UPDATE
    cardRequest = request.card?.toInternal() ?: Card()
}

fun CardContext.fromTransport(request: CardDeleteRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: CardStubCase.NONE
    command = CardCommand.DELETE
    cardRequest = Card(
        guid = request.guid?.let { CardGuid(it) } ?: CardGuid.NONE,
        lockGuid = request.lock?.let { LockGuid(it) } ?: LockGuid.NONE
    )
}

private fun CardRequestWorkModeDto.toInternalWorkMode(): WorkMode = when (this.mode) {
    CardRequestWorkModeDto.Mode.PROD -> WorkMode.PROD
    CardRequestWorkModeDto.Mode.TEST -> WorkMode.TEST
    CardRequestWorkModeDto.Mode.STUB -> WorkMode.STUB
    else -> WorkMode.PROD
}

private fun CardRequestWorkModeDto.toInternalStubCase(): CardStubCase = when (this.stubCase) {
    CardRequestWorkModeDto.StubCase.SUCCESS -> CardStubCase.SUCCESS
    CardRequestWorkModeDto.StubCase.BAD_GUID -> CardStubCase.VALIDATION_ERROR_BAD_GUID
    CardRequestWorkModeDto.StubCase.BAD_NUMBER -> CardStubCase.VALIDATION_ERROR_BAD_NUMBER
    CardRequestWorkModeDto.StubCase.BAD_VALID_FOR -> CardStubCase.VALIDATION_ERROR_BAD_VALID_FOR
    CardRequestWorkModeDto.StubCase.BAD_OWNER -> CardStubCase.VALIDATION_ERROR_BAD_OWNER
    CardRequestWorkModeDto.StubCase.BAD_BANK_GUID -> CardStubCase.VALIDATION_ERROR_BAD_BANK_GUID
    else -> CardStubCase.NONE
}

private fun CardCreateObjectDto.toInternal(): Card {
    return Card(
        number = this.number ?: "",
        validFor = this.validFor ?: "",
        owner = this.owner ?: "",
        bankGuid = this.bank.toBankGuid()
    )
}

private fun CardObjectDto.toInternal(): Card {
    return Card(
        guid = CardGuid(this.guid ?: ""),
        number = this.number ?: "",
        validFor = this.validFor ?: "",
        owner = this.owner ?: "",
        bankGuid = this.bank?.guid?.let { BankGuid(it) } ?: BankGuid.NONE,
        lockGuid = this.lock?.let { LockGuid(it) } ?: LockGuid.NONE
    )
}
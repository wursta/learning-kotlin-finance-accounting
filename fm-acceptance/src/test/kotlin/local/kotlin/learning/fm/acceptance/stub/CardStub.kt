package local.kotlin.learning.fm.acceptance.stub

import local.learning.api.models.*

object CardStub {
    fun getCreateRequest(stubCase: CardRequestWorkModeDto.StubCase): CardCreateRequestDto {
        return CardCreateRequestDto(
            requestType = "cardCreate",
            requestId = "uniqueId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getReadRequest(stubCase: CardRequestWorkModeDto.StubCase): CardReadRequestDto {
        return CardReadRequestDto(
            requestType = "cardRead",
            requestId = "uniqueId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getUpdateRequest(stubCase: CardRequestWorkModeDto.StubCase): CardUpdateRequestDto {
        return CardUpdateRequestDto(
            requestType = "cardUpdate",
            requestId = "uniqueId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getDeleteRequest(stubCase: CardRequestWorkModeDto.StubCase): CardDeleteRequestDto {
        return CardDeleteRequestDto(
            requestType = "cardDelete",
            requestId = "uniqueId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    val createResponse = CardCreateResponseDto(
        responseType = "cardCreate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        card = CardObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            number = "6915542779916368",
            validFor = "2025-12",
            owner = "SAZONOV MIKHAIL",
            bank = BankObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
        )
    )

    val readResponse = CardReadResponseDto(
        responseType = "cardRead",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        card = CardObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            number = "6915542779916368",
            validFor = "2025-12",
            owner = "SAZONOV MIKHAIL",
            bank = BankObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
        )
    )

    val updateResponse = CardUpdateResponseDto(
        responseType = "cardUpdate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        card = CardObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            number = "6915542779916368",
            validFor = "2025-12",
            owner = "SAZONOV MIKHAIL",
            bank = BankObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
        )
    )

    val deleteResponse = CardDeleteResponseDto(
        responseType = "cardDelete",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        card = CardObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            number = "6915542779916368",
            validFor = "2025-12",
            owner = "SAZONOV MIKHAIL",
            bank = BankObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
        )
    )
}
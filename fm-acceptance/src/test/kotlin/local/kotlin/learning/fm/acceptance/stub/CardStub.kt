package local.kotlin.learning.fm.acceptance.stub

import local.learning.api.models.*

object CardStub {
    val createRequest = CardCreateRequestDto(
        requestType = "cardCreate",
        requestId = "uniqueId",
        card = CardCreateObjectDto(
            number = "5191891428863955",
            validFor = "2022-10",
            owner = "MIKHAIL SAZONOV",
            bank = "1598044e-5259-11e9-8647-d663bd873d93"
        )
    )

    val readRequest = CardReadRequestDto(
        requestType = "cardRead",
        requestId = "uniqueId",
        guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )

    val updateRequest = CardUpdateRequestDto(
        requestType = "cardCreate",
        requestId = "uniqueId",
        card = CardObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            number = "5191891428863955",
            validFor = "2022-10",
            owner = "MIKHAIL SAZONOV",
            bank = BankObjectDto(
                guid = "1598044e-5259-11e9-8647-d663bd873d93"
            )
        )
    )

    val deleteRequest = CardDeleteRequestDto(
        requestType = "cardDelete",
        requestId = "uniqueId",
        guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )

    val createResponse = CardCreateResponseDto(
        responseType = "cardCreate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
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
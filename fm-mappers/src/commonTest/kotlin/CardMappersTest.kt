package local.learning.mappers

import local.learning.api.models.*
import local.learning.common.CardContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.WorkMode
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardGuid
import local.learning.common.models.card.CardStubCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CardMappersTest {
    @Test
    fun fromCardCreateRequestTransport() {
        val createReq = CardCreateRequestDto(
            requestId = "uniqueRequestId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.PROD
            ),
            card = CardCreateObjectDto(
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL",
                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
            )
        )

        val context = CardContext()
        context.fromTransport(createReq)

        assertEquals(CardCommand.CREATE, context.command)
        assertEquals(WorkMode.PROD, context.workMode)
        assertEquals("5191891428863955", context.cardRequest.number)
        assertEquals("2023-04", context.cardRequest.validFor)
        assertEquals("SAZONOV MIKHAIL", context.cardRequest.owner)
        assertEquals("e69486d1-dae7-4ade-aa82-b4e98f65f0f2", context.cardRequest.bankGuid.asString())
    }

    @Test
    fun toCardCreateResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.CREATE,
            cardResponse = Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            ),
            errors = mutableListOf(
                Error(
                    code = ErrorCode.INVALID_FIELD_FORMAT,
                    group = ErrorGroup.VALIDATION,
                    field = "number",
                    message = "wrong number",
                )
            )
        )

        val req = context.toTransport() as CardCreateResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("5191891428863955", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)

        assertEquals(1, req.errors?.size)
        assertEquals("invalid_field_format", req.errors?.firstOrNull()?.code)
        assertEquals("validation", req.errors?.firstOrNull()?.group)
        assertEquals("number", req.errors?.firstOrNull()?.field)
        assertEquals("wrong number", req.errors?.firstOrNull()?.message)
    }

    @Test
    fun fromCardReadRequestTransport() {
        val createReq = CardReadRequestDto(
            requestId = "uniqueRequestId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.STUB,
                stubCase = CardRequestWorkModeDto.StubCase.SUCCESS
            ),
            guid = "1598044e-5259-11e9-8647-d663bd873d93"
        )

        val context = CardContext()
        context.fromTransport(createReq)

        assertEquals(CardCommand.READ, context.command)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals(CardStubCase.SUCCESS, context.stubCase)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", context.cardRequest.guid.asString())
    }

    @Test
    fun toCardReadResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.READ,
            cardResponse = Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val req = context.toTransport() as CardReadResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("5191891428863955", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)
    }

    @Test
    fun fromCardUpdateRequestTransport() {
        val req = CardUpdateRequestDto(
            requestId = "uniqueRequestId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.TEST
            ),
            card = CardObjectDto(
                guid = "1598044e-5259-11e9-8647-d663bd873d93",
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL",
                bank = BankObjectDto(
                    guid = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                )
            )
        )

        val context = CardContext()
        context.fromTransport(req)

        assertEquals(CardCommand.UPDATE, context.command)
        assertEquals(WorkMode.TEST, context.workMode)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", context.cardRequest.guid.asString())
        assertEquals("5191891428863955", context.cardRequest.number)
        assertEquals("2023-04", context.cardRequest.validFor)
        assertEquals("SAZONOV MIKHAIL", context.cardRequest.owner)
        assertEquals("e69486d1-dae7-4ade-aa82-b4e98f65f0f2", context.cardRequest.bankGuid.asString())
    }

    @Test
    fun toCardUpdateResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.UPDATE,
            cardResponse = Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val req = context.toTransport() as CardUpdateResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.card?.guid)
        assertEquals("5191891428863955", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)
    }

    @Test
    fun fromCardDeleteRequestTransport() {
        val req = CardDeleteRequestDto(
            requestId = "uniqueRequestId",
            workMode = CardRequestWorkModeDto(
                mode = CardRequestWorkModeDto.Mode.STUB,
                stubCase = CardRequestWorkModeDto.StubCase.BAD_GUID
            ),
            guid = "1598044e-5259-11e9-8647-d663bd873d93"
        )

        val context = CardContext()
        context.fromTransport(req)

        assertEquals(CardCommand.DELETE, context.command)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals(CardStubCase.VALIDATION_ERROR_BAD_GUID, context.stubCase)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", context.cardRequest.guid.asString())
    }

    @Test
    fun toCardDeleteResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.DELETE,
            cardResponse = Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val req = context.toTransport() as CardDeleteResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.card?.guid)
        assertEquals("5191891428863955", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)
    }
}
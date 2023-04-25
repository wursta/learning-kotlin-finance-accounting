package local.learning.mappers.v1

import local.learning.api.v1.models.*
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardContext
import local.learning.common.models.card.CardGuid
import kotlin.test.Test
import kotlin.test.assertEquals

class CardMappersTest {
    @Test
    fun fromCardCreateRequestTransport() {
        val createReq = CardCreateRequestDto(
            requestId = "uniqueRequestId",
            card = CardCreateObjectDto(
                number = "1234-5678-9102-3456",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val context = CardContext()
        context.fromTransport(createReq)

        assertEquals(CardCommand.CREATE, context.command)
        assertEquals("1234-5678-9102-3456", context.cardRequest.number)
        assertEquals("2023-04", context.cardRequest.validFor)
        assertEquals("SAZONOV MIKHAIL", context.cardRequest.owner)
    }

    @Test
    fun toCardCreateResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.CREATE,
            cardResponse = Card(
                guid = CardGuid("uniqueGUID"),
                number = "1234-5678-9102-3456",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            ),
            errors = mutableListOf(
                Error(
                    code = "err",
                    group = "request",
                    field = "number",
                    message = "wrong number",
                )
            )
        )

        val req = context.toTransport() as CardCreateResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("1234-5678-9102-3456", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("number", req.errors?.firstOrNull()?.field)
        assertEquals("wrong number", req.errors?.firstOrNull()?.message)
    }

    @Test
    fun fromCardReadRequestTransport() {
        val createReq = CardReadRequestDto(
            requestId = "uniqueRequestId",
            guid = "uniqueGUID"
        )

        val context = CardContext()
        context.fromTransport(createReq)

        assertEquals(CardCommand.READ, context.command)
        assertEquals("uniqueGUID", context.cardRequest.guid.asString())
    }

    @Test
    fun toCardReadResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.READ,
            cardResponse = Card(
                guid = CardGuid("uniqueGUID"),
                number = "1234-5678-9102-3456",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val req = context.toTransport() as CardReadResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("1234-5678-9102-3456", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)
    }

    @Test
    fun fromCardUpdateRequestTransport() {
        val req = CardUpdateRequestDto(
            requestId = "uniqueRequestId",
            card = CardObjectDto(
                guid = "uniqueGUID",
                number = "1234-5678-9102-3456",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val context = CardContext()
        context.fromTransport(req)

        assertEquals(CardCommand.UPDATE, context.command)
        assertEquals("uniqueGUID", context.cardRequest.guid.asString())
        assertEquals("1234-5678-9102-3456", context.cardRequest.number)
        assertEquals("2023-04", context.cardRequest.validFor)
        assertEquals("SAZONOV MIKHAIL", context.cardRequest.owner)
    }

    @Test
    fun toCardUpdateResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.UPDATE,
            cardResponse = Card(
                guid = CardGuid("uniqueGUID"),
                number = "1234-5678-9102-3456",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val req = context.toTransport() as CardUpdateResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("uniqueGUID", req.card?.guid)
        assertEquals("1234-5678-9102-3456", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)
    }

    @Test
    fun fromCardDeleteRequestTransport() {
        val req = CardDeleteRequestDto(
            requestId = "uniqueRequestId",
            guid = "uniqueGUID"
        )

        val context = CardContext()
        context.fromTransport(req)

        assertEquals(CardCommand.DELETE, context.command)
        assertEquals("uniqueGUID", context.cardRequest.guid.asString())
    }

    @Test
    fun toCardDeleteResponseTransport() {
        val context = CardContext(
            requestId = RequestId("uniqueReqID"),
            command = CardCommand.DELETE,
            cardResponse = Card(
                guid = CardGuid("uniqueGUID"),
                number = "1234-5678-9102-3456",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL"
            )
        )

        val req = context.toTransport() as CardDeleteResponseDto
        assertEquals("uniqueReqID", req.requestId)
        assertEquals("uniqueGUID", req.card?.guid)
        assertEquals("1234-5678-9102-3456", req.card?.number)
        assertEquals("2023-04", req.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", req.card?.owner)
    }
}
package local.learning.api.v1

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import local.learning.api.serialization.utils.jsonSerializer
import local.learning.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CardResponseSerializationTest {
    private val createResponse: IResponseDto = CardCreateResponseDto(
        responseType = "cardCreate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        card = CardObjectDto(
            guid = "12345",
            number = "123",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL"
        )
    )

    @Test
    fun serialize() {
        val createJson = jsonSerializer.encodeToString(createResponse)

        assertTrue(createJson.contains(Regex("\"number\":\\s*\"123\"")))
        assertTrue(createJson.contains(Regex("\"valid_for\":\\s*\"2026-04\"")))
        assertTrue(createJson.contains(Regex("\"owner\":\\s*\"SAZONOV MIKHAIL\"")))
    }

    @Test
    fun deserialize() {
        val createJson = jsonSerializer.encodeToString(createResponse)

        val obj = jsonSerializer.decodeFromString(createJson) as IResponseDto
        assertIs<CardCreateResponseDto>(obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
                "responseType":"cardCreate",
                "requestId":"uniqueId",
                "result": "success",
                "errors": [],
                "card": {
                    "guid": "12345",
                    "number":"123",
                    "valid_for": "2026-04",
                    "owner":"SAZONOV MIKHAIL"
                }
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("uniqueId", obj.requestId)
        assertEquals(createResponse, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
package local.learning.api

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import local.learning.api.models.*
import local.learning.api.serialization.utils.jsonSerializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CardResponseSerializationTest {
    private val createResponse: IResponseDto = CardCreateResponseDto(
        responseType = "cardCreate",
        requestId = "40f009d6-fdc3-4a70-b7f8-98d1e4904756",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        card = CardObjectDto(
            guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
            number = "5191891428863955",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL",
            bank = BankObjectDto(
                guid = "428488f0-b878-4080-a2fa-6fbdb5d7790a",
                name = "Sberbank"
            )
        )
    )

    @Test
    fun serialize() {
        val createJson = jsonSerializer.encodeToString(createResponse)

        assertTrue(createJson.contains(Regex("\"guid\":\\s*\"eb770343-0339-43a9-aee9-54dccba2cf8e\"")))
        assertTrue(createJson.contains(Regex("\"number\":\\s*\"5191891428863955\"")))
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
                "requestId":"40f009d6-fdc3-4a70-b7f8-98d1e4904756",
                "result": "success",
                "errors": [],
                "card": {
                    "guid": "eb770343-0339-43a9-aee9-54dccba2cf8e",
                    "number":"5191891428863955",
                    "valid_for": "2026-04",
                    "owner":"SAZONOV MIKHAIL",
                    "bank": {
                        "guid": "428488f0-b878-4080-a2fa-6fbdb5d7790a",
                        "name": "Sberbank"
                    }
                }
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("40f009d6-fdc3-4a70-b7f8-98d1e4904756", obj.requestId)
        assertEquals(createResponse, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
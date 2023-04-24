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

class CardRequestSerializationTest {
    private val createRequest: IRequestDto = CardCreateRequestDto(
        requestType = "cardCreate",
        requestId = "uniqueId",
        card = CardCreateObjectDto(
            number = "123",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL"
        )
    )

    private val readRequest: IRequestDto = CardReadRequestDto(
        requestType = "cardRead",
        requestId = "uniqueId",
        guid = "12345"
    )

    private val updateRequest: IRequestDto = CardUpdateRequestDto(
        requestType = "cardUpdate",
        requestId = "uniqueId",
        card = CardObjectDto(
            guid = "12345",
            number = "123",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL"
        )
    )

    private val deleteRequest: IRequestDto = CardDeleteRequestDto(
        requestType = "cardDelete",
        requestId = "uniqueId",
        guid = "12345"
    )

    @Test
    fun serialize() {
        val createJson = jsonSerializer.encodeToString(createRequest)
        assertTrue(createJson.contains(Regex("\"number\":\\s*\"123\"")))
        assertTrue(createJson.contains(Regex("\"valid_for\":\\s*\"2026-04\"")))
        assertTrue(createJson.contains(Regex("\"owner\":\\s*\"SAZONOV MIKHAIL\"")))

        val readJson = jsonSerializer.encodeToString(readRequest)
        assertTrue(readJson.contains(Regex("\"guid\":\\s*\"12345\"")))

        val updateJson = jsonSerializer.encodeToString(updateRequest)
        assertTrue(updateJson.contains(Regex("\"guid\":\\s*\"12345\"")))
        assertTrue(updateJson.contains(Regex("\"number\":\\s*\"123\"")))
        assertTrue(updateJson.contains(Regex("\"valid_for\":\\s*\"2026-04\"")))
        assertTrue(updateJson.contains(Regex("\"owner\":\\s*\"SAZONOV MIKHAIL\"")))

        val deleteJson = jsonSerializer.encodeToString(deleteRequest)
        assertTrue(deleteJson.contains(Regex("\"guid\":\\s*\"12345\"")))
    }

    @Test
    fun deserialize() {
        val createJson = jsonSerializer.encodeToString(createRequest)
        val createRequest = jsonSerializer.decodeFromString(createJson) as IRequestDto
        assertIs<CardCreateRequestDto>(createRequest)

        val readJson = jsonSerializer.encodeToString(readRequest)
        val readRequest = jsonSerializer.decodeFromString(readJson) as IRequestDto
        assertIs<CardReadRequestDto>(readRequest)

        val updateJson = jsonSerializer.encodeToString(updateRequest)
        val updateRequest = jsonSerializer.decodeFromString(updateJson) as IRequestDto
        assertIs<CardUpdateRequestDto>(updateRequest)

        val deleteJson = jsonSerializer.encodeToString(deleteRequest)
        val deleteRequest = jsonSerializer.decodeFromString(deleteJson) as IRequestDto
        assertIs<CardDeleteRequestDto>(deleteRequest)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
                "requestType":"cardCreate",
                "requestId":"uniqueId",                                            
                "card": {
                    "number":"123",
                    "valid_for": "2026-04",
                    "owner":"SAZONOV MIKHAIL"
                }
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IRequestDto

        assertEquals("uniqueId", obj.requestId)
        assertEquals(createRequest, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
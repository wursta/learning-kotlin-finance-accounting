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

class CardRequestSerializationTest {
    private val createRequest: IRequestDto = CardCreateRequestDto(
        requestType = "cardCreate",
        requestId = "db630ffa-85e2-4cb0-9c15-23f662e36685",
        card = CardCreateObjectDto(
            number = "5191891428863955",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL",
            bank = "428488f0-b878-4080-a2fa-6fbdb5d7790a"
        )
    )

    private val readRequest: IRequestDto = CardReadRequestDto(
        requestType = "cardRead",
        requestId = "uniqueId",
        guid = "eb770343-0339-43a9-aee9-54dccba2cf8e"
    )

    private val updateRequest: IRequestDto = CardUpdateRequestDto(
        requestType = "cardUpdate",
        requestId = "db630ffa-85e2-4cb0-9c15-23f662e36685",
        card = CardObjectDto(
            guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
            number = "5191891428863955",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL",
            bank = BankObjectDto(
                guid = "428488f0-b878-4080-a2fa-6fbdb5d7790a"
            )
        )
    )

    private val deleteRequest: IRequestDto = CardDeleteRequestDto(
        requestType = "cardDelete",
        requestId = "db630ffa-85e2-4cb0-9c15-23f662e36685",
        guid = "eb770343-0339-43a9-aee9-54dccba2cf8e"
    )

    @Test
    fun serialize() {
        val createJson = jsonSerializer.encodeToString(createRequest)
        assertTrue(createJson.contains(Regex("\"number\":\\s*\"5191891428863955\"")))
        assertTrue(createJson.contains(Regex("\"valid_for\":\\s*\"2026-04\"")))
        assertTrue(createJson.contains(Regex("\"owner\":\\s*\"SAZONOV MIKHAIL\"")))
        assertTrue(createJson.contains(Regex("\"bank\":\\s*\"428488f0-b878-4080-a2fa-6fbdb5d7790a\"")))

        val readJson = jsonSerializer.encodeToString(readRequest)
        assertTrue(readJson.contains(Regex("\"guid\":\\s*\"eb770343-0339-43a9-aee9-54dccba2cf8e\"")))

        val updateJson = jsonSerializer.encodeToString(updateRequest)
        assertTrue(updateJson.contains(Regex("\"guid\":\\s*\"eb770343-0339-43a9-aee9-54dccba2cf8e\"")))
        assertTrue(updateJson.contains(Regex("\"number\":\\s*\"5191891428863955\"")))
        assertTrue(updateJson.contains(Regex("\"valid_for\":\\s*\"2026-04\"")))
        assertTrue(updateJson.contains(Regex("\"owner\":\\s*\"SAZONOV MIKHAIL\"")))

        val deleteJson = jsonSerializer.encodeToString(deleteRequest)
        assertTrue(deleteJson.contains(Regex("\"guid\":\\s*\"eb770343-0339-43a9-aee9-54dccba2cf8e\"")))
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
                "requestId":"db630ffa-85e2-4cb0-9c15-23f662e36685",                                            
                "card": {
                    "number":"5191891428863955",
                    "valid_for": "2026-04",
                    "owner":"SAZONOV MIKHAIL",
                    "bank": "428488f0-b878-4080-a2fa-6fbdb5d7790a"
                }
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IRequestDto

        assertEquals("db630ffa-85e2-4cb0-9c15-23f662e36685", obj.requestId)
        assertEquals(createRequest, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
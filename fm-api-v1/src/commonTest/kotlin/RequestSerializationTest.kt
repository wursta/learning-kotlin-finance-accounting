package local.learning.api.v1

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import local.learning.api.serialization.utils.card.jsonSerializer
import local.learning.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RequestSerializationTest {
    private val createRequest: IRequest = CardCreateRequest(
        requestType = "create",
        requestId = "uniqueId",
        card = CardCreateObject(
            number = "123",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL"
        )
    )

    private val readRequest: IRequest = CardReadRequest(
        requestType = "read",
        requestId = "uniqueId",
        guid = "12345"
    )

    private val updateRequest: IRequest = CardUpdateRequest(
        requestType = "update",
        requestId = "uniqueId",
        card = CardObject(
            guid = "12345",
            number = "123",
            validFor = "2026-04",
            owner = "SAZONOV MIKHAIL"
        )
    )

    private val deleteRequest: IRequest = CardDeleteRequest(
        requestType = "delete",
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
        val createRequest = jsonSerializer.decodeFromString<CardCreateRequest>(createJson)
        assertIs<CardCreateRequest>(createRequest)

        val readJson = jsonSerializer.encodeToString(readRequest)
        val readRequest = jsonSerializer.decodeFromString<CardReadRequest>(readJson)
        assertIs<CardReadRequest>(readRequest)

        val updateJson = jsonSerializer.encodeToString(updateRequest)
        val updateRequest = jsonSerializer.decodeFromString<CardUpdateRequest>(updateJson)
        assertIs<CardUpdateRequest>(updateRequest)

        val deleteJson = jsonSerializer.encodeToString(deleteRequest)
        val deleteRequest = jsonSerializer.decodeFromString<CardDeleteRequest>(deleteJson)
        assertIs<CardDeleteRequest>(deleteRequest)
    }

    //@Test(expected = SerializationException::class)
    @Test
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        val createRequest = jsonSerializer.decodeFromString<CardCreateRequest>(json)
        assertNull(createRequest.requestType)
    }
}
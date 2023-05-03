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

class ExpenseRequestSerializationTest {
    private val request: IRequestDto = ExpenseCreateRequestDto(
        requestType = "expenseCreate",
        requestId = "1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
        expense = ExpenseCreateObjectDto(
            createDt = "2023-04-24T19:09:06.043Z",
            amount = 100.40,
            card = "c9b8c97d-c66e-4eb0-851f-8dafadd1af56",
            category = "e7021137-6995-4870-9522-5a0abe554ada"
        )
    )

    private val searchRequest: IRequestDto = ExpenseSearchRequestDto(
        requestType = "expensesSearch",
        requestId = "1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
        cards = listOf("c9b8c97d-c66e-4eb0-851f-8dafadd1af56", "2ae4dffc-5506-4783-8a44-c73156b86165"),
        amountFrom = 50.5,
        dateFrom = "2023-04-24T00:00:00.043Z"
    )

    private val statsRequest: IRequestDto = ExpenseStatsRequestDto(
        requestType = "expensesStats",
        requestId = "1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
        dateFrom = "2023-04-24T00:00:00.043Z",
        dateTo = "2023-04-24T23:59:59.043Z"
    )
    @Test
    fun serialize() {
        val json = jsonSerializer.encodeToString(request)
        assertTrue(json.contains(Regex("\"create_dt\":\\s*\"2023-04-24T19:09:06.043Z\"")))
        assertTrue(json.contains(Regex("\"amount\":\\s*100.4")))
        assertTrue(json.contains(Regex("\"card\":\\s*\"c9b8c97d-c66e-4eb0-851f-8dafadd1af56\"")))
        assertTrue(json.contains(Regex("\"category\":\\s*\"e7021137-6995-4870-9522-5a0abe554ada\"")))

        val searchJson = jsonSerializer.encodeToString(searchRequest)
        assertTrue(searchJson.contains(Regex("\"amount_from\":\\s*50.5")))
        assertTrue(searchJson.contains(Regex("\"amount_to\":\\s*null")))
        assertTrue(searchJson.contains(Regex("\"date_from\":\\s*\"2023-04-24T00:00:00.043Z\"")))
        assertTrue(searchJson.contains(Regex("\"date_to\":\\s*null")))
        assertTrue(searchJson.contains(Regex("\"cards\":\\s*\\[\"c9b8c97d-c66e-4eb0-851f-8dafadd1af56\",\"2ae4dffc-5506-4783-8a44-c73156b86165\"]")))

        val statsJson = jsonSerializer.encodeToString(statsRequest)
        assertTrue(statsJson.contains(Regex("\"date_from\":\\s*\"2023-04-24T00:00:00.043Z\"")))
        assertTrue(statsJson.contains(Regex("\"date_to\":\\s*\"2023-04-24T23:59:59.043Z\"")))
    }

    @Test
    fun deserialize() {
        val createJson = jsonSerializer.encodeToString(request)
        val createRequest = jsonSerializer.decodeFromString(createJson) as IRequestDto
        assertIs<ExpenseCreateRequestDto>(createRequest)

        val searchJson = jsonSerializer.encodeToString(searchRequest)
        val searchRequest = jsonSerializer.decodeFromString(searchJson) as IRequestDto
        assertIs<ExpenseSearchRequestDto>(searchRequest)

        val statsJson = jsonSerializer.encodeToString(statsRequest)
        val statsRequest = jsonSerializer.decodeFromString(statsJson) as IRequestDto
        assertIs<ExpenseStatsRequestDto>(statsRequest)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
                "requestType":"expensesStats",
                "requestId":"1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",                                            
                "date_from": "2023-04-24T00:00:00.043Z",
                "date_to": "2023-04-24T23:59:59.043Z"
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IRequestDto

        assertEquals("1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9", obj.requestId)
        assertEquals(statsRequest, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
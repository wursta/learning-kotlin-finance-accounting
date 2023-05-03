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

class ExpenseResponseSerializationTest {
    private val createResponse: IResponseDto = ExpenseCreateResponseDto(
        responseType = "expenseCreate",
        requestId = "1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expense = ExpenseObjectDto(
            guid = "3d4b3225-f379-4380-89fb-f8cfbe6722ad",
            createDt = "2023-04-24T19:09:06",
            amount = 100.5,
            card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
            category = "d08e4713-657e-4351-b983-536c1bae51b5",
        )
    )

    private val searchResponse: IResponseDto = ExpenseSearchResponseDto(
        responseType = "expensesSearch",
        requestId = "1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expenses = listOf(
            ExpenseObjectDto(
                guid = "3d4b3225-f379-4380-89fb-f8cfbe6722ad",
                createDt = "2023-04-24T19:09:06",
                amount = 100.5,
                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                category = "d08e4713-657e-4351-b983-536c1bae51b5",
            ),
            ExpenseObjectDto(
                guid = "256fee33-5deb-4cc4-906f-db970ae3d277",
                createDt = "2023-04-24T20:09:06",
                amount = 50.0,
                card = "8d295974-d106-413c-acc9-59584b3b11ed",
                category = "8fe236f1-2877-4180-a966-148e740ca0be",
            )
        )
    )

    private val statsResponse: IResponseDto = ExpenseStatsResponseDto(
        responseType = "expensesStats",
        requestId = "1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        total = 5000.0,
        summary = listOf(
            ExpenseStatSummaryItemDto(
                category = CategoryObjectDto(
                    guid = "d08e4713-657e-4351-b983-536c1bae51b5",
                    name = "categoryName"
                ),
                amount = 500.0
            ),
            ExpenseStatSummaryItemDto(
                category = CategoryObjectDto(
                    guid = "8fe236f1-2877-4180-a966-148e740ca0be",
                    name = "categoryName2"
                ),
                amount = 1500.0
            )
        )

    )

    @Test
    fun serialize() {
        val createJson = jsonSerializer.encodeToString(createResponse)
        assertTrue(createJson.contains(Regex("\"guid\":\\s*\"3d4b3225-f379-4380-89fb-f8cfbe6722ad\"")))
        assertTrue(createJson.contains(Regex("\"create_dt\":\\s*\"2023-04-24T19:09:06\"")))
        assertTrue(createJson.contains(Regex("\"amount\":\\s*100.5")))
        assertTrue(createJson.contains(Regex("\"card\":\\s*\"a8585ea8-e039-4799-b16c-06dc92f641f9\"")))
        assertTrue(createJson.contains(Regex("\"category\":\\s*\"d08e4713-657e-4351-b983-536c1bae51b5\"")))
    }

    @Test
    fun deserialize() {
        val createJson = jsonSerializer.encodeToString(createResponse)

        val obj = jsonSerializer.decodeFromString(createJson) as IResponseDto
        assertIs<ExpenseCreateResponseDto>(obj)

        val searchJson = jsonSerializer.encodeToString(searchResponse)
        val searchObj = jsonSerializer.decodeFromString(searchJson) as IResponseDto
        assertIs<ExpenseSearchResponseDto>(searchObj)

        val statsJson = jsonSerializer.encodeToString(statsResponse)
        val statsObj = jsonSerializer.decodeFromString(statsJson) as IResponseDto
        assertIs<ExpenseStatsResponseDto>(statsObj)
    }

    @Test
    fun deserializeNakedCreate() {
        val jsonString = """
            {
                "responseType":"expenseCreate",
                "requestId":"1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
                "result": "success",
                "errors": [],
                "expense": {
                    "guid": "3d4b3225-f379-4380-89fb-f8cfbe6722ad",
                    "create_dt":"2023-04-24T19:09:06",
                    "amount": 100.5,
                    "card":"a8585ea8-e039-4799-b16c-06dc92f641f9",
                    "category":"d08e4713-657e-4351-b983-536c1bae51b5"
                }
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9", obj.requestId)
        assertEquals(createResponse, obj)
    }

    @Test
    fun deserializeNakedSearch() {
        val jsonString = """
            {
                "responseType":"expensesSearch",
                "requestId":"1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
                "result": "success",
                "errors": [],
                "expenses": [
                    {
                        "guid": "3d4b3225-f379-4380-89fb-f8cfbe6722ad",
                        "create_dt":"2023-04-24T19:09:06",
                        "amount": 100.5,
                        "card":"a8585ea8-e039-4799-b16c-06dc92f641f9",
                        "category":"d08e4713-657e-4351-b983-536c1bae51b5"
                    },
                    {
                        "guid": "256fee33-5deb-4cc4-906f-db970ae3d277",
                        "create_dt":"2023-04-24T20:09:06",
                        "amount": 50,
                        "card":"8d295974-d106-413c-acc9-59584b3b11ed",
                        "category":"8fe236f1-2877-4180-a966-148e740ca0be"
                    }
                ]
            }
        """.trimIndent()

        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9", obj.requestId)
        assertEquals(searchResponse, obj)
    }

    @Test
    fun deserializeNakedStats() {
        val jsonString = """
            {
                "responseType":"expensesStats",
                "requestId":"1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9",
                "result": "success",
                "errors": [],
                "total": 5000,              
                "summary": [
                    {
                        "category": {
                            "guid": "d08e4713-657e-4351-b983-536c1bae51b5",
                            "name": "categoryName"
                        },
                        "amount": 500                       
                    },
                    {
                        "category": {
                            "guid": "8fe236f1-2877-4180-a966-148e740ca0be",
                            "name": "categoryName2"
                        },
                        "amount": 1500                    
                    }
                ]
            }
        """.trimIndent()

        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("1bdf67a7-59ed-4ff1-aa32-cb06d0c9b8f9", obj.requestId)
        assertEquals(statsResponse, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
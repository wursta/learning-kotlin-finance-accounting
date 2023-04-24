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

class ExpenseResponseSerializationTest {
    private val createResponse: IResponseDto = ExpenseCreateResponseDto(
        responseType = "expenseCreate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expense = ExpenseObjectDto(
            guid = "expenseGuid",
            createDt = "2023-04-24T19:09:06",
            amount = 100.5F,
            source = "cardGUID",
            category = "categoryGUID",
        )
    )

    private val searchResponse: IResponseDto = ExpenseSearchResponseDto(
        responseType = "expensesSearch",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expenses = listOf(
            ExpenseObjectDto(
                guid = "expenseGuid",
                createDt = "2023-04-24T19:09:06",
                amount = 100.5F,
                source = "cardGUID",
                category = "categoryGUID",
            ),
            ExpenseObjectDto(
                guid = "expenseGuid2",
                createDt = "2023-04-24T20:09:06",
                amount = 50F,
                source = "cardGUID2",
                category = "categoryGUID",
            )
        )
    )

    private val statsResponse: IResponseDto = ExpenseStatsResponseDto(
        responseType = "expensesStats",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        total = 5000F,
        summary = listOf(
            ExpenseStatSummaryItemDto(
                category = CategoryObjectDto(
                    guid = "categoryGUID",
                    name = "categoryName"
                ),
                amount = 500F
            ),
            ExpenseStatSummaryItemDto(
                category = CategoryObjectDto(
                    guid = "categoryGUID2",
                    name = "categoryName2"
                ),
                amount = 1500F
            )
        )

    )

    @Test
    fun serialize() {
        val createJson = jsonSerializer.encodeToString(createResponse)
        assertTrue(createJson.contains(Regex("\"guid\":\\s*\"expenseGuid\"")))
        assertTrue(createJson.contains(Regex("\"create_dt\":\\s*\"2023-04-24T19:09:06\"")))
        assertTrue(createJson.contains(Regex("\"amount\":\\s*100.5")))
        assertTrue(createJson.contains(Regex("\"source\":\\s*\"cardGUID\"")))
        assertTrue(createJson.contains(Regex("\"category\":\\s*\"categoryGUID\"")))
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
                "requestId":"uniqueId",
                "result": "success",
                "errors": [],
                "expense": {
                    "guid": "expenseGuid",
                    "create_dt":"2023-04-24T19:09:06",
                    "amount": 100.5,
                    "source":"cardGUID",
                    "category":"categoryGUID"
                }
            }
        """.trimIndent()
        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("uniqueId", obj.requestId)
        assertEquals(createResponse, obj)
    }

    @Test
    fun deserializeNakedSearch() {
        val jsonString = """
            {
                "responseType":"expensesSearch",
                "requestId":"uniqueId",
                "result": "success",
                "errors": [],
                "expenses": [
                    {
                        "guid": "expenseGuid",
                        "create_dt":"2023-04-24T19:09:06",
                        "amount": 100.5,
                        "source":"cardGUID",
                        "category":"categoryGUID"
                    },
                    {
                        "guid": "expenseGuid2",
                        "create_dt":"2023-04-24T20:09:06",
                        "amount": 50,
                        "source":"cardGUID2",
                        "category":"categoryGUID"
                    }
                ]
            }
        """.trimIndent()

        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("uniqueId", obj.requestId)
        assertEquals(searchResponse, obj)
    }

    @Test
    fun deserializeNakedStats() {
        val jsonString = """
            {
                "responseType":"expensesStats",
                "requestId":"uniqueId",
                "result": "success",
                "errors": [],
                "total": 5000,              
                "summary": [
                    {
                        "category": {
                            "guid": "categoryGUID",
                            "name": "categoryName"
                        },
                        "amount": 500                       
                    },
                    {
                        "category": {
                            "guid": "categoryGUID2",
                            "name": "categoryName2"
                        },
                        "amount": 1500                    
                    }
                ]
            }
        """.trimIndent()

        val obj = jsonSerializer.decodeFromString(jsonString) as IResponseDto

        assertEquals("uniqueId", obj.requestId)
        assertEquals(statsResponse, obj)
    }

    @Test(expected = SerializationException::class)
    fun tryDeserializeDummy() {
        val json = "{\"a\":1}"
        jsonSerializer.decodeFromString(json) as IRequestDto
    }
}
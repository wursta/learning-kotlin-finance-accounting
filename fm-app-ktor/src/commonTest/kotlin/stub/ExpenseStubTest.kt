package local.learning.app.ktor.stub

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import local.learning.api.models.*
import local.learning.api.serialization.utils.jsonSerializer
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseStatistic
import local.learning.stubs.ExpenseStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ExpenseStubTest {
    private val expenseStub: Expense = ExpenseStub.get()
    private val expensesListStub: List<Expense> = ExpenseStub.getList()
    private val expensesStatsStub: ExpenseStatistic = ExpenseStub.getStatistic()
    @Test
    fun create() = testApplication {
        val response = client.post("/api/expense/create") {
            val requestObj = ExpenseCreateRequestDto(
                requestId = "uniqueRequestId",
                requestType = "expenseCreate",
                workMode = ExpenseRequestWorkModeDto(
                    mode = ExpenseRequestWorkModeDto.Mode.STUB,
                    stubCase = ExpenseRequestWorkModeDto.StubCase.SUCCESS
                ),
                expense = ExpenseCreateObjectDto(
                    amount = expenseStub.amount.toDouble(),
                    card = expenseStub.cardGuid.asString(),
                    category = expenseStub.categoryGuid.asString()
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = jsonSerializer.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = jsonSerializer.decodeFromString<ExpenseCreateResponseDto>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("expenseCreate", responseObj.responseType)
        assertEquals(ResponseResultDto.SUCCESS, responseObj.result)
        assertEquals(0, responseObj.errors?.size)
        assertEquals(expenseStub.guid.asString(), responseObj.expense?.guid)
        assertEquals(expenseStub.amount.toDouble(), responseObj.expense?.amount)
        assertEquals(expenseStub.cardGuid.asString(), responseObj.expense?.card)
        assertEquals(expenseStub.categoryGuid.asString(), responseObj.expense?.category)
    }

    @Test
    fun read() = testApplication {
        val response = client.post("/api/expense/read") {
            val requestObj = ExpenseReadRequestDto(
                requestId = "uniqueRequestId",
                requestType = "expenseRead",
                workMode = ExpenseRequestWorkModeDto(
                    mode = ExpenseRequestWorkModeDto.Mode.STUB,
                    stubCase = ExpenseRequestWorkModeDto.StubCase.SUCCESS
                ),
                guid = expenseStub.guid.asString()
            )
            contentType(ContentType.Application.Json)
            val requestJson = jsonSerializer.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = jsonSerializer.decodeFromString<ExpenseReadResponseDto>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("expenseRead", responseObj.responseType)
        assertEquals(ResponseResultDto.SUCCESS, responseObj.result)
        assertEquals(0, responseObj.errors?.size)
        assertEquals(expenseStub.guid.asString(), responseObj.expense?.guid)
        assertEquals(expenseStub.amount.toDouble(), responseObj.expense?.amount)
        assertEquals(expenseStub.cardGuid.asString(), responseObj.expense?.card)
        assertEquals(expenseStub.categoryGuid.asString(), responseObj.expense?.category)
    }

    @Test
    fun update() = testApplication {
        val response = client.post("/api/expense/update") {
            val requestObj = ExpenseUpdateRequestDto(
                requestId = "uniqueRequestId",
                requestType = "expenseUpdate",
                workMode = ExpenseRequestWorkModeDto(
                    mode = ExpenseRequestWorkModeDto.Mode.STUB,
                    stubCase = ExpenseRequestWorkModeDto.StubCase.SUCCESS
                ),
                expense = ExpenseObjectDto(
                    guid = expenseStub.guid.asString(),
                    amount = expenseStub.amount.toDouble(),
                    card = expenseStub.cardGuid.asString(),
                    category = expenseStub.categoryGuid.asString()
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = jsonSerializer.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = jsonSerializer.decodeFromString<ExpenseUpdateResponseDto>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("expenseUpdate", responseObj.responseType)
        assertEquals(ResponseResultDto.SUCCESS, responseObj.result)
        assertEquals(0, responseObj.errors?.size)
        assertEquals(expenseStub.guid.asString(), responseObj.expense?.guid)
        assertEquals(expenseStub.amount.toDouble(), responseObj.expense?.amount)
        assertEquals(expenseStub.cardGuid.asString(), responseObj.expense?.card)
        assertEquals(expenseStub.categoryGuid.asString(), responseObj.expense?.category)
    }

    @Test
    fun delete() = testApplication {
        val response = client.post("/api/expense/delete") {
            val requestObj = ExpenseDeleteRequestDto(
                requestId = "uniqueRequestId",
                requestType = "expenseDelete",
                workMode = ExpenseRequestWorkModeDto(
                    mode = ExpenseRequestWorkModeDto.Mode.STUB,
                    stubCase = ExpenseRequestWorkModeDto.StubCase.SUCCESS
                ),
                guid = expenseStub.guid.asString()
            )
            contentType(ContentType.Application.Json)
            val requestJson = jsonSerializer.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = jsonSerializer.decodeFromString<ExpenseDeleteResponseDto>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("expenseDelete", responseObj.responseType)
        assertEquals(ResponseResultDto.SUCCESS, responseObj.result)
        assertEquals(0, responseObj.errors?.size)
        assertEquals(expenseStub.guid.asString(), responseObj.expense?.guid)
        assertEquals(expenseStub.amount.toDouble(), responseObj.expense?.amount)
        assertEquals(expenseStub.cardGuid.asString(), responseObj.expense?.card)
        assertEquals(expenseStub.categoryGuid.asString(), responseObj.expense?.category)
    }

    @Test
    fun search() = testApplication {
        val response = client.post("/api/expense/search") {
            val requestObj = ExpenseSearchRequestDto(
                requestId = "uniqueRequestId",
                requestType = "expensesSearch",
                workMode = ExpenseRequestWorkModeDto(
                    mode = ExpenseRequestWorkModeDto.Mode.STUB,
                    stubCase = ExpenseRequestWorkModeDto.StubCase.SUCCESS
                ),
                amountFrom = 100.00
            )
            contentType(ContentType.Application.Json)
            val requestJson = jsonSerializer.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = jsonSerializer.decodeFromString<ExpenseSearchResponseDto>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("expensesSearch", responseObj.responseType)
        assertEquals(ResponseResultDto.SUCCESS, responseObj.result)
        assertEquals(0, responseObj.errors?.size)
        assertNotNull(responseObj.expenses)
        assertEquals(expensesListStub.size, responseObj.expenses?.size)

        val respExpensesList: List<ExpenseObjectDto> = responseObj.expenses ?: emptyList()
        for ((i, expense) in respExpensesList.withIndex()) {
            assertEquals(expensesListStub[i].guid.asString(), expense.guid)
            assertEquals(expensesListStub[i].amount.toDouble(), expense.amount)
            assertEquals(expensesListStub[i].cardGuid.asString(), expense.card)
            assertEquals(expensesListStub[i].categoryGuid.asString(), expense.category)
        }
    }

    @Test
    fun stats() = testApplication {
        val response = client.post("/api/expense/stats") {
            val requestObj = ExpenseStatsRequestDto(
                requestId = "uniqueRequestId",
                requestType = "expensesStats",
                workMode = ExpenseRequestWorkModeDto(
                    mode = ExpenseRequestWorkModeDto.Mode.STUB,
                    stubCase = ExpenseRequestWorkModeDto.StubCase.SUCCESS
                ),
            )
            contentType(ContentType.Application.Json)
            val requestJson = jsonSerializer.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = jsonSerializer.decodeFromString<ExpenseStatsResponseDto>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("expensesStats", responseObj.responseType)
        assertEquals(ResponseResultDto.SUCCESS, responseObj.result)
        assertEquals(0, responseObj.errors?.size)
        assertEquals(expensesStatsStub.total.toDouble(), responseObj.total)
        assertNotNull(responseObj.summary)
        assertEquals(expensesStatsStub.summaryByCategory.size, responseObj.summary?.size)

        val respSummaryList: List<ExpenseStatSummaryItemDto> = responseObj.summary ?: emptyList()
        for ((i, summary) in respSummaryList.withIndex()) {
            assertEquals(expensesStatsStub.summaryByCategory[i].amount.toDouble(), summary.amount)
            assertEquals(expensesStatsStub.summaryByCategory[i].category.guid.asString(), summary.category?.guid)
            assertEquals(expensesStatsStub.summaryByCategory[i].category.name, summary.category?.name)
        }
    }
}
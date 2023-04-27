package local.learning.mappers

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import local.learning.api.models.*
import local.learning.common.ExpenseContext
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.Category
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseMappersTest {
    @Test
    fun fromExpenseCreateRequestTransport() {
        val req = ExpenseCreateRequestDto(
            requestId = "uniqueRequestId",
            expense = ExpenseCreateObjectDto(
                createDt = "2023-01-01T14:46:04Z",
                amount = 540.4F,
                card = "1598044e-5259-11e9-8647-d663bd873d93",
                category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
            )
        )

        val context = ExpenseContext()
        context.fromTransport(req)

        assertEquals(ExpenseCommand.CREATE, context.command)
        assertEquals(Instant.parse("2023-01-01T14:46:04Z"), context.expenseRequest.createDT)
        assertEquals(540.4F, context.expenseRequest.amount)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), context.expenseRequest.cardGuid)
        assertEquals(CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"), context.expenseRequest.categoryGuid)
    }
    @Test
    fun toExpenseCreateResponseTransport() {
        val now = Clock.System.now()
        val context = ExpenseContext(
            requestId = RequestId("uniqueReqID"),
            command = ExpenseCommand.CREATE,
            expenseResponse = Expense(
                guid = ExpenseGuid("uniqueGUID"),
                createDT = now,
                amount = 1234.56F,
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
            ),
            errors = mutableListOf(
                Error(
                    code = "err",
                    group = "request",
                    field = "amount",
                    message = "wrong amount",
                )
            )
        )

        val req = context.toTransport() as ExpenseCreateResponseDto

        assertEquals("uniqueReqID", req.requestId)
        assertEquals(now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), req.expense?.createDt)
        assertEquals(1234.56F, req.expense?.amount)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.expense?.card)
        assertEquals("5410bdaf-834a-4ca6-9044-ee25d5a7164c", req.expense?.category)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("amount", req.errors?.firstOrNull()?.field)
        assertEquals("wrong amount", req.errors?.firstOrNull()?.message)
    }

    @Test
    fun fromExpenseReadRequestTransport() {
        val req = ExpenseReadRequestDto(
            requestId = "uniqueRequestId",
            guid = "cardGuid"
        )

        val context = ExpenseContext()
        context.fromTransport(req)

        assertEquals(ExpenseCommand.READ, context.command)
        assertEquals(ExpenseGuid("cardGuid"), context.expenseRequest.guid)
    }
    @Test
    fun toExpenseReadResponseTransport() {
        val now = Clock.System.now()
        val context = ExpenseContext(
            requestId = RequestId("uniqueReqID"),
            command = ExpenseCommand.READ,
            expenseResponse = Expense(
                guid = ExpenseGuid("uniqueGUID"),
                createDT = now,
                amount = 1234.56F,
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
            )
        )

        val req = context.toTransport() as ExpenseReadResponseDto

        assertEquals("uniqueReqID", req.requestId)
        assertEquals(now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), req.expense?.createDt)
        assertEquals(1234.56F, req.expense?.amount)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.expense?.card)
        assertEquals("5410bdaf-834a-4ca6-9044-ee25d5a7164c", req.expense?.category)
    }
    @Test
    fun fromExpenseUpdateRequestTransport() {
        val req = ExpenseUpdateRequestDto(
            requestId = "uniqueRequestId",
            expense = ExpenseObjectDto(
                guid = "expenseGuid",
                createDt = "2023-01-01T14:46:04Z",
                amount = 540.4F,
                card = "1598044e-5259-11e9-8647-d663bd873d93",
                category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
            )
        )

        val context = ExpenseContext()
        context.fromTransport(req)

        assertEquals(ExpenseCommand.UPDATE, context.command)
        assertEquals(ExpenseGuid("expenseGuid"), context.expenseRequest.guid)
        assertEquals(Instant.parse("2023-01-01T14:46:04Z"), context.expenseRequest.createDT)
        assertEquals(540.4F, context.expenseRequest.amount)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), context.expenseRequest.cardGuid)
        assertEquals(CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"), context.expenseRequest.categoryGuid)
    }
    @Test
    fun toExpenseUpdateResponseTransport() {
        val now = Clock.System.now()
        val context = ExpenseContext(
            requestId = RequestId("uniqueReqID"),
            command = ExpenseCommand.UPDATE,
            expenseResponse = Expense(
                guid = ExpenseGuid("uniqueGUID"),
                createDT = now,
                amount = 1234.56F,
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
            )
        )

        val req = context.toTransport() as ExpenseUpdateResponseDto

        assertEquals("uniqueReqID", req.requestId)
        assertEquals(now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), req.expense?.createDt)
        assertEquals(1234.56F, req.expense?.amount)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.expense?.card)
        assertEquals("5410bdaf-834a-4ca6-9044-ee25d5a7164c", req.expense?.category)
    }

    @Test
    fun fromExpenseDeleteRequestTransport() {
        val req = ExpenseDeleteRequestDto(
            requestId = "uniqueRequestId",
            guid = "expenseGuid"
        )

        val context = ExpenseContext()
        context.fromTransport(req)

        assertEquals(ExpenseCommand.DELETE, context.command)
        assertEquals(ExpenseGuid("expenseGuid"), context.expenseRequest.guid)
    }
    @Test
    fun toExpenseDeleteResponseTransport() {
        val now = Clock.System.now()
        val context = ExpenseContext(
            requestId = RequestId("uniqueReqID"),
            command = ExpenseCommand.DELETE,
            expenseResponse = Expense(
                guid = ExpenseGuid("uniqueGUID"),
                createDT = now,
                amount = 1234.56F,
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
            )
        )

        val req = context.toTransport() as ExpenseDeleteResponseDto

        assertEquals("uniqueReqID", req.requestId)
        assertEquals(now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), req.expense?.createDt)
        assertEquals(1234.56F, req.expense?.amount)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.expense?.card)
        assertEquals("5410bdaf-834a-4ca6-9044-ee25d5a7164c", req.expense?.category)
    }

    @Test
    fun fromExpenseSearchRequestTransport() {
        val req = ExpenseSearchRequestDto(
            requestId = "uniqueRequestId",
            amountFrom = 100F,
            dateFrom = "2023-01-01T14:46:04Z",
            cards = mutableListOf("1598044e-5259-11e9-8647-d663bd873d93", "66c9ee23-87ca-4104-b1fd-67acb650a595")
        )

        val context = ExpenseContext()
        context.fromTransport(req)

        assertEquals(ExpenseCommand.SEARCH, context.command)
        assertEquals(100F, context.expenseSearchRequest.amountFrom)
        assertEquals(Float.POSITIVE_INFINITY, context.expenseSearchRequest.amountTo)
        assertEquals(Instant.parse("2023-01-01T14:46:04Z"), context.expenseSearchRequest.dateFrom)
        assertEquals(INSTANT_POSITIVE_INFINITY, context.expenseSearchRequest.dateTo)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), context.expenseSearchRequest.sources[0])
        assertEquals(CardGuid("66c9ee23-87ca-4104-b1fd-67acb650a595"), context.expenseSearchRequest.sources[1])
    }
    @Test
    fun toExpenseSearchResponseTransport() {
        val now = Clock.System.now()
        val context = ExpenseContext(
            requestId = RequestId("uniqueReqID"),
            command = ExpenseCommand.SEARCH,
            expenseSearchResponse = mutableListOf(
                Expense(
                    guid = ExpenseGuid("uniqueGUID"),
                    createDT = now,
                    amount = 1234.56F,
                    cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                    categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
                ),
                Expense(
                    guid = ExpenseGuid("uniqueGUID2"),
                    createDT = now,
                    amount = 7890.12F,
                    cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                    categoryGuid = CategoryGuid("793d16e5-4259-4540-adbf-1313da3bbcc2")
                )
            )
        )

        val req = context.toTransport() as ExpenseSearchResponseDto

        assertEquals("uniqueReqID", req.requestId)

        assertEquals(2, req.expenses?.size)

        assertEquals("uniqueGUID", req.expenses?.firstOrNull()?.guid)
        assertEquals(now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), req.expenses?.firstOrNull()?.createDt)
        assertEquals(1234.56F, req.expenses?.firstOrNull()?.amount)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.expenses?.firstOrNull()?.card)
        assertEquals("5410bdaf-834a-4ca6-9044-ee25d5a7164c", req.expenses?.firstOrNull()?.category)

        assertEquals("uniqueGUID2", req.expenses?.lastOrNull()?.guid)
        assertEquals(now.toLocalDateTime(TimeZone.currentSystemDefault()).toString(), req.expenses?.lastOrNull()?.createDt)
        assertEquals(7890.12F, req.expenses?.lastOrNull()?.amount)
        assertEquals("1598044e-5259-11e9-8647-d663bd873d93", req.expenses?.lastOrNull()?.card)
        assertEquals("793d16e5-4259-4540-adbf-1313da3bbcc2", req.expenses?.lastOrNull()?.category)
    }

    @Test
    fun fromExpenseStatsRequestTransport() {
        val req = ExpenseStatsRequestDto(
            requestId = "uniqueRequestId",
            dateFrom = "2023-01-01T14:46:04Z"
        )

        val context = ExpenseContext()
        context.fromTransport(req)

        assertEquals(ExpenseCommand.STATS, context.command)
        assertEquals(Instant.parse("2023-01-01T14:46:04Z"), context.expenseStatisticRequest.dateFrom)
        assertEquals(INSTANT_POSITIVE_INFINITY, context.expenseStatisticRequest.dateTo)
    }
    @Test
    fun toExpenseStatsResponseTransport() {
        val context = ExpenseContext(
            requestId = RequestId("uniqueReqID"),
            command = ExpenseCommand.STATS,
            expenseStatisticResponse = ExpenseStatistic(
                total = 50343.24F,
                summaryByCategory = mutableListOf(
                    ExpenseSummaryByCategory(
                        category = Category(
                            guid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"),
                            name = "Category Name 1"
                        ),
                        amount = 10533.23F
                    ),
                    ExpenseSummaryByCategory(
                        category = Category(
                            guid = CategoryGuid("6deee98f-883b-4339-be9f-e5f8c60f1066"),
                            name = "Category Name 2"
                        ),
                        amount = 3434.23F
                    ),
                    ExpenseSummaryByCategory(
                        category = Category(
                            guid = CategoryGuid("7d688712-0ebe-4fbd-91f3-229632baac2a"),
                            name = "Category Name 3"
                        ),
                        amount = 5343.3F
                    )
                )
            )
        )

        val req = context.toTransport() as ExpenseStatsResponseDto

        assertEquals("uniqueReqID", req.requestId)

        assertEquals(3, req.summary?.size)
        assertEquals(50343.24F, req.total)

        assertEquals("5410bdaf-834a-4ca6-9044-ee25d5a7164c", req.summary?.get(0)?.category?.guid)
        assertEquals("Category Name 1", req.summary?.get(0)?.category?.name)
        assertEquals(10533.23F, req.summary?.get(0)?.amount)

        assertEquals("6deee98f-883b-4339-be9f-e5f8c60f1066", req.summary?.get(1)?.category?.guid)
        assertEquals("Category Name 2", req.summary?.get(1)?.category?.name)
        assertEquals(3434.23F, req.summary?.get(1)?.amount)

        assertEquals("7d688712-0ebe-4fbd-91f3-229632baac2a", req.summary?.get(2)?.category?.guid)
        assertEquals("Category Name 3", req.summary?.get(2)?.category?.name)
        assertEquals(5343.3F, req.summary?.get(2)?.amount)
    }
}
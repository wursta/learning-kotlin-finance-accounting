package local.learning.app.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import local.learning.common.ExpenseContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.State
import local.learning.common.models.WorkMode
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.*
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseValidationTest {
    private val processor = ExpenseProcessor()

    @Test
    fun createValid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                createDT = Clock.System.now(),
                amount = BigDecimal(1234.56),
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.expenseRequest, ctx.expenseValidating)
    }

    @Test
    fun createInvalid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                createDT = Clock.System.now(),
                amount = BigDecimal(0),
                cardGuid = CardGuid("wrong_guid"),
                categoryGuid = CategoryGuid("wrong_guid")
            )
        )

        processor.exec(ctx)

        assertEquals(3, ctx.errors.size)
        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("amount", ctx.errors[0].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[1].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[1].group)
        assertEquals("card", ctx.errors[1].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[2].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[2].group)
        assertEquals("category", ctx.errors[2].field)
    }

    @Test
    fun readValid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.READ,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93")
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.expenseRequest, ctx.expenseValidating)
    }

    @Test
    fun readInvalid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.READ,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                guid = ExpenseGuid("wrong guid")
            )
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }

    @Test
    fun updateValid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.UPDATE,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                createDT = Clock.System.now(),
                amount = BigDecimal(1234.56),
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.expenseRequest, ctx.expenseValidating)
    }

    @Test
    fun updateInvalid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.UPDATE,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                guid = ExpenseGuid("wrong guid"),
                createDT = Clock.System.now(),
                amount = BigDecimal(0),
                cardGuid = CardGuid("wrong guid"),
                categoryGuid = CategoryGuid("wrong guid")
            )
        )

        processor.exec(ctx)

        assertEquals(4, ctx.errors.size)
        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[1].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[1].group)
        assertEquals("amount", ctx.errors[1].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[2].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[2].group)
        assertEquals("card", ctx.errors[2].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[3].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[3].group)
        assertEquals("category", ctx.errors[3].field)
    }

    @Test
    fun deleteValid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.DELETE,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93")
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.expenseRequest, ctx.expenseValidating)
    }

    @Test
    fun deleteInvalid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.DELETE,
            workMode = WorkMode.PROD,
            expenseRequest = Expense(
                guid = ExpenseGuid("wrong guid")
            )
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }

    @Test
    fun searchValid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.SEARCH,
            workMode = WorkMode.PROD,
            expenseSearchRequest = ExpenseSearchFilter(
                amountFrom = BigDecimal.valueOf(100),
                amountTo = BigDecimal.valueOf(200),
                dateFrom = Instant.parse("2023-01-01T14:46:04Z"),
                dateTo = Instant.parse("2023-01-02T14:46:04Z"),
                sources = listOf(
                    CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                    CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                )
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.expenseSearchRequest, ctx.expenseSearchValidating)
    }

    @Test
    fun searchInvalid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.SEARCH,
            workMode = WorkMode.PROD,
            expenseSearchRequest = ExpenseSearchFilter(
                amountFrom = BigDecimal.valueOf(200),
                amountTo = BigDecimal.valueOf(100),
                dateFrom = Instant.parse("2023-01-02T14:46:04Z"),
                dateTo = Instant.parse("2023-01-01T14:46:04Z"),
                sources = listOf(
                    CardGuid("wrong_guid"),
                    CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                )
            )
        )

        processor.exec(ctx)

        assertEquals(5, ctx.errors.size)
        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("amount_from", ctx.errors[0].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[1].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[1].group)
        assertEquals("amount_to", ctx.errors[1].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[2].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[2].group)
        assertEquals("date_from", ctx.errors[2].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[3].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[3].group)
        assertEquals("date_to", ctx.errors[3].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[4].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[4].group)
        assertEquals("sources", ctx.errors[4].field)
    }

    @Test
    fun statisticValid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.STATS,
            workMode = WorkMode.PROD,
            expenseStatisticRequest = ExpenseStatisticFilter(
                dateFrom = Instant.parse("2023-01-01T14:46:04Z"),
                dateTo = Instant.parse("2023-01-02T14:46:04Z"),
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.expenseStatisticRequest, ctx.expenseStatisticValidating)
    }

    @Test
    fun statisticInvalid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.STATS,
            workMode = WorkMode.PROD,
            expenseStatisticRequest = ExpenseStatisticFilter(
                dateFrom = Instant.parse("2023-01-02T14:46:04Z"),
                dateTo = Instant.parse("2023-01-01T14:46:04Z"),
            )
        )

        processor.exec(ctx)

        assertEquals(2, ctx.errors.size)
        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("date_from", ctx.errors[0].field)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[1].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[1].group)
        assertEquals("date_to", ctx.errors[1].field)
    }
}
package local.learning.app.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import local.learning.common.ExpenseContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.WorkMode
import local.learning.common.models.expense.ExpenseCommand
import local.learning.common.models.expense.ExpenseStubCase
import local.learning.stubs.ExpenseStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseStubsTest {
    private val processor = ExpenseProcessor()
    private val expenseStub = ExpenseStub.get()
    private val expenseSearchStub = ExpenseStub.getList()
    private val expenseStatisticStub = ExpenseStub.getStatistic()

    @Test
    fun noStubCase() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.NONE
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_STUB_CASE, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
    }

    @Test
    fun createSuccess() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(expenseStub.guid, ctx.expenseResponse.guid)
        assertEquals(expenseStub.amount, ctx.expenseResponse.amount)
        assertEquals(expenseStub.cardGuid, ctx.expenseResponse.cardGuid)
        assertEquals(expenseStub.categoryGuid, ctx.expenseResponse.categoryGuid)
    }

    @Test
    fun createValidationErrorBadAmount() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_AMOUNT
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("amount", ctx.errors[0].field)
    }

    @Test
    fun createValidationErrorBadCardGuid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_CARD_GUID
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("card", ctx.errors[0].field)
    }

    @Test
    fun createValidationErrorBadCategoryGuid() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.CREATE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_CATEGORY_GUID
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("category", ctx.errors[0].field)
    }

    @Test
    fun readSuccess() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.READ,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(expenseStub.guid, ctx.expenseResponse.guid)
        assertEquals(expenseStub.amount, ctx.expenseResponse.amount)
        assertEquals(expenseStub.cardGuid, ctx.expenseResponse.cardGuid)
        assertEquals(expenseStub.categoryGuid, ctx.expenseResponse.categoryGuid)
    }

    @Test
    fun updateSuccess() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.UPDATE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(expenseStub.guid, ctx.expenseResponse.guid)
        assertEquals(expenseStub.amount, ctx.expenseResponse.amount)
        assertEquals(expenseStub.cardGuid, ctx.expenseResponse.cardGuid)
        assertEquals(expenseStub.categoryGuid, ctx.expenseResponse.categoryGuid)
    }

    @Test
    fun deleteSuccess() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.DELETE,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(expenseStub.guid, ctx.expenseResponse.guid)
        assertEquals(expenseStub.amount, ctx.expenseResponse.amount)
        assertEquals(expenseStub.cardGuid, ctx.expenseResponse.cardGuid)
        assertEquals(expenseStub.categoryGuid, ctx.expenseResponse.categoryGuid)
    }

    @Test
    fun searchSuccess() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.SEARCH,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(expenseSearchStub.size, ctx.expenseSearchResponse.size)

        expenseSearchStub.forEachIndexed { index, expense ->
            assertEquals(expense.guid, ctx.expenseSearchResponse[index].guid)
            assertEquals(expense.amount, ctx.expenseSearchResponse[index].amount)
            assertEquals(expense.cardGuid, ctx.expenseSearchResponse[index].cardGuid)
            assertEquals(expense.categoryGuid, ctx.expenseSearchResponse[index].categoryGuid)
        }
    }

    @Test
    fun searchValidationErrorsSearchFilterAmountFrom() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.SEARCH,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_AMOUNT_FROM
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("amount_from", ctx.errors[0].field)
    }

    @Test
    fun searchValidationErrorsSearchFilterAmountTo() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.SEARCH,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_AMOUNT_TO
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("amount_to", ctx.errors[0].field)
    }

    @Test
    fun searchValidationErrorsSearchFilterSources() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.SEARCH,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_SOURCES
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("sources", ctx.errors[0].field)
    }

    @Test
    fun statsSuccess() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.STATS,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(expenseStatisticStub.total, ctx.expenseStatisticResponse.total)
        assertEquals(expenseStatisticStub.summaryByCategory.size, ctx.expenseStatisticResponse.summaryByCategory.size)

        expenseStatisticStub.summaryByCategory.forEachIndexed { index, summary ->
            assertEquals(summary.amount, ctx.expenseStatisticResponse.summaryByCategory[index].amount)
            assertEquals(summary.category, ctx.expenseStatisticResponse.summaryByCategory[index].category)
        }
    }

    @Test
    fun searchValidationErrorsStatisticFilterDateFrom() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.STATS,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_STATISTIC_FILTER_DATE_FROM
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("date_from", ctx.errors[0].field)
    }

    @Test
    fun searchValidationErrorsStatisticFilterDateTo() = runTest {
        val ctx = ExpenseContext(
            command = ExpenseCommand.STATS,
            workMode = WorkMode.STUB,
            stubCase = ExpenseStubCase.VALIDATION_ERROR_BAD_STATISTIC_FILTER_DATE_TO
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("date_to", ctx.errors[0].field)
    }
}
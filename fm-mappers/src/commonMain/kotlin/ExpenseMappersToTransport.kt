package local.learning.mappers

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import local.learning.api.models.*
import local.learning.common.ExpenseContext
import local.learning.common.models.Error
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.Category
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseCommand
import local.learning.common.models.expense.ExpenseGuid
import local.learning.common.models.expense.ExpenseSummaryByCategory
import local.learning.mappers.exceptions.UnknownExpenseCommand
import java.math.BigDecimal

fun ExpenseContext.toTransport(): IResponseDto = when (val cmd = command) {
    ExpenseCommand.CREATE -> toTransportCreate()
    ExpenseCommand.READ -> toTransportRead()
    ExpenseCommand.UPDATE -> toTransportUpdate()
    ExpenseCommand.DELETE -> toTransportDelete()
    ExpenseCommand.SEARCH -> toTransportSearch()
    ExpenseCommand.STATS -> toTransportStats()
    ExpenseCommand.NONE -> throw UnknownExpenseCommand(cmd)
}

fun ExpenseContext.toTransportCreate() = ExpenseCreateResponseDto(
    responseType = "expenseCreate",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    expense = expenseResponse.toTransport()
)

fun ExpenseContext.toTransportRead() = ExpenseReadResponseDto(
    responseType = "expenseRead",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    expense = expenseResponse.toTransport()
)

fun ExpenseContext.toTransportUpdate() = ExpenseUpdateResponseDto(
    responseType = "expenseUpdate",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    expense = expenseResponse.toTransport()
)

fun ExpenseContext.toTransportDelete() = ExpenseDeleteResponseDto(
    responseType = "expenseDelete",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    expense = expenseResponse.toTransport()
)

fun ExpenseContext.toTransportSearch() = ExpenseSearchResponseDto(
    responseType = "expensesSearch",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    expenses = expenseSearchResponse.toExpensesListTransport()
)

fun ExpenseContext.toTransportStats() = ExpenseStatsResponseDto(
    responseType = "expensesStats",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = ResponseResultDto.SUCCESS,
    errors = errors.toTransportErrors(),
    total = expenseStatisticResponse.total.toDouble(),
    summary = expenseStatisticResponse.summaryByCategory.toExpensesSummaryByCategoryTransport()
)

private fun Expense.toTransport(): ExpenseObjectDto = ExpenseObjectDto(
    guid = guid.takeIf { it != ExpenseGuid.NONE }?.asString(),
    createDt = createDT.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
    amount = amount.takeIf { it != BigDecimal.ZERO }?.toDouble(),
    card = cardGuid.takeIf { it != CardGuid.NONE }?.asString(),
    category = categoryGuid.takeIf { it != CategoryGuid.NONE }?.asString(),
)

private fun Category.toTransport(): CategoryObjectDto = CategoryObjectDto(
    guid = guid.takeIf { it != CategoryGuid.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() }
)

private fun List<Expense>.toExpensesListTransport(): List<ExpenseObjectDto>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ExpenseSummaryByCategory.toTransport(): ExpenseStatSummaryItemDto = ExpenseStatSummaryItemDto(
    category = category.toTransport(),
    amount = amount.toDouble()
)

private fun List<ExpenseSummaryByCategory>.toExpensesSummaryByCategoryTransport(): List<ExpenseStatSummaryItemDto>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<Error>.toTransportErrors(): List<ResponseErrorDto>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Error.toTransport() = ResponseErrorDto(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
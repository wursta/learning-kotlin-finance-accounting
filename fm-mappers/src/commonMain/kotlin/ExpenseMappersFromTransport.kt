package local.learning.mappers

import kotlinx.datetime.Instant
import local.learning.api.models.*
import local.learning.common.ExpenseContext
import local.learning.common.INSTANT_NEGATIVE_INFINITY
import local.learning.common.INSTANT_NONE
import local.learning.common.INSTANT_POSITIVE_INFINITY
import local.learning.common.models.LockGuid
import local.learning.common.models.RequestId
import local.learning.common.models.WorkMode
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.*
import local.learning.mappers.exceptions.UnknownRequestClass
import java.math.BigDecimal

private fun IRequestDto?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE
private fun String?.toExpenseGuid() = this?.let { ExpenseGuid(it) } ?: ExpenseGuid.NONE
private fun String?.toExpenseWithGuId() = Expense(guid = this.toExpenseGuid())
private fun String?.toCardGuid() = this?.let { CardGuid(it) } ?: CardGuid.NONE
private fun String?.toCategoryGuid() = this?.let { CategoryGuid(it) } ?: CategoryGuid.NONE
private fun String?.toLockGuid() = this?.let { LockGuid(it) } ?: LockGuid.NONE
private fun String?.toInternalSourceGuid(): CardGuid = this?.let { CardGuid(it) } ?: CardGuid.NONE
private fun List<String?>.toInternalSourcesGuidsList(): List<CardGuid>? = this
    .map { it.toInternalSourceGuid() }
    .toList()
    .takeIf { it.isNotEmpty() }
@Suppress("unused")
fun ExpenseContext.fromTransport(request: IRequestDto) = when (request) {
    is ExpenseCreateRequestDto -> fromTransport(request)
    is ExpenseReadRequestDto -> fromTransport(request)
    is ExpenseUpdateRequestDto -> fromTransport(request)
    is ExpenseDeleteRequestDto -> fromTransport(request)
    is ExpenseSearchRequestDto -> fromTransport(request)
    is ExpenseStatsRequestDto -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

fun ExpenseContext.fromTransport(request: ExpenseCreateRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: ExpenseStubCase.NONE
    command = ExpenseCommand.CREATE
    expenseRequest = request.expense?.toInternal() ?: Expense()
}

fun ExpenseContext.fromTransport(request: ExpenseReadRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: ExpenseStubCase.NONE
    command = ExpenseCommand.READ
    expenseRequest = request.guid.toExpenseWithGuId()
}

fun ExpenseContext.fromTransport(request: ExpenseUpdateRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: ExpenseStubCase.NONE
    command = ExpenseCommand.UPDATE
    expenseRequest = request.expense?.toInternal() ?: Expense()
}

fun ExpenseContext.fromTransport(request: ExpenseDeleteRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: ExpenseStubCase.NONE
    command = ExpenseCommand.DELETE
    expenseRequest = Expense(
        guid = request.guid.toExpenseGuid(),
        lockGuid = request.lock.toLockGuid()
    )
}

fun ExpenseContext.fromTransport(request: ExpenseSearchRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: ExpenseStubCase.NONE
    command = ExpenseCommand.SEARCH
    expenseSearchRequest = ExpenseSearchFilter(
        amountFrom = request.amountFrom.takeIf { it != null }?.let { BigDecimal(it) } ?: BigDecimal.valueOf(-1),
        amountTo = request.amountTo.takeIf { it != null }?.let { BigDecimal(it) } ?: BigDecimal.valueOf(-1),
        dateFrom = request.dateFrom?.let { Instant.parse(it) } ?: INSTANT_NEGATIVE_INFINITY,
        dateTo = request.dateTo?.let { Instant.parse(it) } ?: INSTANT_POSITIVE_INFINITY,
        sources = request.cards?.toInternalSourcesGuidsList() ?: mutableListOf()
    )
}

fun ExpenseContext.fromTransport(request: ExpenseStatsRequestDto) {
    requestId = request.requestId()
    workMode = request.workMode?.toInternalWorkMode() ?: WorkMode.PROD
    stubCase = request.workMode?.toInternalStubCase() ?: ExpenseStubCase.NONE
    command = ExpenseCommand.STATS
    expenseStatisticRequest = ExpenseStatisticFilter(
        dateFrom = request.dateFrom?.let { Instant.parse(it) } ?: INSTANT_NEGATIVE_INFINITY,
        dateTo = request.dateTo?.let { Instant.parse(it) } ?: INSTANT_POSITIVE_INFINITY,
    )
}

private fun ExpenseRequestWorkModeDto.toInternalWorkMode(): WorkMode = when(this.mode) {
    ExpenseRequestWorkModeDto.Mode.PROD -> WorkMode.PROD
    ExpenseRequestWorkModeDto.Mode.TEST -> WorkMode.TEST
    ExpenseRequestWorkModeDto.Mode.STUB -> WorkMode.STUB
    else -> WorkMode.PROD
}

private fun ExpenseRequestWorkModeDto.toInternalStubCase(): ExpenseStubCase = when(this.stubCase) {
    ExpenseRequestWorkModeDto.StubCase.SUCCESS -> ExpenseStubCase.SUCCESS
    ExpenseRequestWorkModeDto.StubCase.BAD_GUID -> ExpenseStubCase.VALIDATION_ERROR_BAD_GUID
    ExpenseRequestWorkModeDto.StubCase.BAD_AMOUNT -> ExpenseStubCase.VALIDATION_ERROR_BAD_AMOUNT
    ExpenseRequestWorkModeDto.StubCase.BAD_CARD_GUID -> ExpenseStubCase.VALIDATION_ERROR_BAD_CARD_GUID
    ExpenseRequestWorkModeDto.StubCase.BAD_CATEGORY_GUID -> ExpenseStubCase.VALIDATION_ERROR_BAD_CATEGORY_GUID
    ExpenseRequestWorkModeDto.StubCase.BAD_SEARCH_FILTER_AMOUNT_FROM -> ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_AMOUNT_FROM
    ExpenseRequestWorkModeDto.StubCase.BAD_SEARCH_FILTER_AMOUNT_TO -> ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_AMOUNT_TO
    ExpenseRequestWorkModeDto.StubCase.BAD_SEARCH_FILTER_SOURCES -> ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_SOURCES
    ExpenseRequestWorkModeDto.StubCase.BAD_STATISTIC_FILTER_DATE_FROM -> ExpenseStubCase.VALIDATION_ERROR_BAD_STATISTIC_FILTER_DATE_FROM
    ExpenseRequestWorkModeDto.StubCase.BAD_STATISTIC_FILTER_DATE_TO -> ExpenseStubCase.VALIDATION_ERROR_BAD_STATISTIC_FILTER_DATE_TO
    else -> ExpenseStubCase.NONE
}

private fun ExpenseCreateObjectDto.toInternal(): Expense {
    return Expense(
        createDT = createDt?.let { Instant.parse(it) } ?: INSTANT_NONE,
        amount = this.amount.takeIf { it != null }?.let { BigDecimal(it) } ?: BigDecimal.ZERO,
        cardGuid = this.card.toCardGuid(),
        categoryGuid = this.category.toCategoryGuid()
    )
}

private fun ExpenseObjectDto.toInternal(): Expense {
    return Expense(
        guid = guid.toExpenseGuid(),
        createDT = createDt?.let { Instant.parse(it) } ?: INSTANT_NONE,
        amount = this.amount.takeIf { it != null }?.let { BigDecimal(it) } ?: BigDecimal.ZERO,
        cardGuid = this.card.toCardGuid(),
        categoryGuid = this.category.toCategoryGuid(),
        lockGuid = this.lock.toLockGuid()
    )
}
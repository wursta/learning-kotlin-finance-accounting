package local.learning.mappers

import kotlinx.datetime.Instant
import local.learning.api.models.*
import local.learning.common.ExpenseContext
import local.learning.common.INSTANT_NEGATIVE_INFINITY
import local.learning.common.INSTANT_NONE
import local.learning.common.INSTANT_POSITIVE_INFINITY
import local.learning.common.helpers.isAmountValid
import local.learning.common.helpers.isCardGuidValid
import local.learning.common.helpers.isCategoryGuidValid
import local.learning.common.helpers.isGuidValid
import local.learning.common.models.RequestId
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.*
import local.learning.mappers.exceptions.InvalidFieldFormat
import local.learning.mappers.exceptions.UnknownRequestClass
import java.math.BigDecimal

private fun IRequestDto?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE
private fun String?.toExpenseGuid() = this?.let { ExpenseGuid(it) } ?: ExpenseGuid.NONE
private fun String?.toExpenseWithGuId() = Expense(guid = this.toExpenseGuid())
private fun String?.toCardGuid() = this?.let { CardGuid(it) } ?: CardGuid.NONE
private fun String?.toCategoryGuid() = this?.let { CategoryGuid(it) } ?: CategoryGuid.NONE
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
    command = ExpenseCommand.CREATE
    expenseRequest = request.expense?.toInternal() ?: Expense()
}

fun ExpenseContext.fromTransport(request: ExpenseReadRequestDto) {
    requestId = request.requestId()
    command = ExpenseCommand.READ
    expenseRequest = request.guid.toExpenseWithGuId()
}

fun ExpenseContext.fromTransport(request: ExpenseUpdateRequestDto) {
    requestId = request.requestId()
    command = ExpenseCommand.UPDATE
    expenseRequest = request.expense?.toInternal() ?: Expense()
}

fun ExpenseContext.fromTransport(request: ExpenseDeleteRequestDto) {
    requestId = request.requestId()
    command = ExpenseCommand.DELETE
    expenseRequest = request.guid.toExpenseWithGuId()
}

fun ExpenseContext.fromTransport(request: ExpenseSearchRequestDto) {
    requestId = request.requestId()
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
    command = ExpenseCommand.STATS
    expenseStatisticRequest = ExpenseStatisticFilter(
        dateFrom = request.dateFrom?.let { Instant.parse(it) } ?: INSTANT_NEGATIVE_INFINITY,
        dateTo = request.dateTo?.let { Instant.parse(it) } ?: INSTANT_POSITIVE_INFINITY,
    )
}

private fun ExpenseCreateObjectDto.toInternal(): Expense {
    return Expense(
        createDT = createDt?.let { Instant.parse(it) } ?: INSTANT_NONE,
        amount = this.amount.takeIf { it != null }?.let { BigDecimal(it) } ?: BigDecimal.ZERO,
        cardGuid = this.card.toCardGuid(),
        categoryGuid = this.category.toCategoryGuid()
    ).also {
        if (!it.isAmountValid()) {
            throw InvalidFieldFormat("amount", "Float >= 0")
        }

        if (!it.isCardGuidValid()) {
            throw InvalidFieldFormat("card", "1598044e-5259-11e9-8647-d663bd873d93")
        }

        if (!it.isCategoryGuidValid()) {
            throw InvalidFieldFormat("category", "1598044e-5259-11e9-8647-d663bd873d93")
        }
    }
}

private fun ExpenseObjectDto.toInternal(): Expense {
    return Expense(
        guid = guid.toExpenseGuid(),
        createDT = createDt?.let { Instant.parse(it) } ?: INSTANT_NONE,
        amount = this.amount.takeIf { it != null }?.let { BigDecimal(it) } ?: BigDecimal.ZERO,
        cardGuid = this.card.toCardGuid(),
        categoryGuid = this.category.toCategoryGuid()
    ).also {
        if (!it.isGuidValid()) {
            throw InvalidFieldFormat("guid", "1598044e-5259-11e9-8647-d663bd873d93")
        }

        if (!it.isAmountValid()) {
            throw InvalidFieldFormat("amount", "Float >= 0")
        }

        if (!it.isCardGuidValid()) {
            throw InvalidFieldFormat("card", "1598044e-5259-11e9-8647-d663bd873d93")
        }

        if (!it.isCategoryGuidValid()) {
            throw InvalidFieldFormat("category", "1598044e-5259-11e9-8647-d663bd873d93")
        }
    }
}
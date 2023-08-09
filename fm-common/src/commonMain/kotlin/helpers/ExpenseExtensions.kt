package local.learning.common.helpers

import local.learning.common.INSTANT_NEGATIVE_INFINITY
import local.learning.common.INSTANT_POSITIVE_INFINITY
import local.learning.common.models.Principal
import local.learning.common.models.expense.*
import java.math.BigDecimal

fun Expense.isGuidValid(): Boolean = guid.isValid()
fun Expense.isAmountValid(): Boolean = amount > BigDecimal.ZERO
fun Expense.isCardGuidValid(): Boolean = cardGuid.isValid()
fun Expense.isCategoryGuidValid(): Boolean = categoryGuid.isValid()
fun ExpenseSearchFilter.isAmountFromValid(): Boolean {
    if (amountFrom == BigDecimal.valueOf(-1)) {
        return true
    }

    if (amountFrom != BigDecimal.valueOf(-1) && amountTo != BigDecimal.valueOf(-1)) {
        return amountFrom > BigDecimal.ZERO && amountFrom < amountTo
    }

    return amountFrom > BigDecimal.ZERO
}

fun ExpenseSearchFilter.isAmountToValid(): Boolean {
    if (amountTo == BigDecimal.valueOf(-1)) {
        return true
    }

    if (amountFrom != BigDecimal.valueOf(-1) && amountTo != BigDecimal.valueOf(-1)) {
        return amountTo > BigDecimal.ZERO && amountTo > amountFrom
    }

    return amountTo > BigDecimal.ZERO
}

fun ExpenseSearchFilter.isDatesValid(): Boolean {
    if (dateFrom != INSTANT_NEGATIVE_INFINITY && dateTo != INSTANT_POSITIVE_INFINITY) {
        return dateFrom < dateTo
    }

    return true
}
fun ExpenseSearchFilter.isSourcesValid(): Boolean = sources.all { it.isValid() }

fun ExpenseStatisticFilter.isDatesValid(): Boolean {
    if (dateFrom != INSTANT_NEGATIVE_INFINITY && dateTo != INSTANT_POSITIVE_INFINITY) {
        return dateFrom < dateTo
    }

    return true
}
fun Expense.resolveRelationsTo(principal: Principal): Set<ExpenseRelation> = setOfNotNull(
    ExpenseRelation.NONE,
    ExpenseRelation.NEW.takeIf { guid == ExpenseGuid.NONE },
    ExpenseRelation.OWN.takeIf { principal.id == createdBy }
)
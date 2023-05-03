package local.learning.common.helpers

import local.learning.common.models.expense.Expense
import java.math.BigDecimal

fun Expense.isGuidValid(): Boolean = guid.isValid()
fun Expense.isAmountValid(): Boolean = amount > BigDecimal.ZERO
fun Expense.isCardGuidValid(): Boolean = cardGuid.isValid()
fun Expense.isCategoryGuidValid(): Boolean = categoryGuid.isValid()
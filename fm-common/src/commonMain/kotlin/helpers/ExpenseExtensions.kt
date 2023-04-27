package local.learning.common.helpers

import local.learning.common.models.expense.Expense

fun Expense.isGuidValid(): Boolean = guid.isValid()
fun Expense.isAmountValid(): Boolean = amount > 0F
fun Expense.isCardGuidValid(): Boolean = cardGuid.isValid()
fun Expense.isCategoryGuidValid(): Boolean = categoryGuid.isValid()
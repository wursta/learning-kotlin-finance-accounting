package local.learning.common.repo.expense

import local.learning.common.models.expense.Expense

data class DbExpenseRequest(
    val expense: Expense
)

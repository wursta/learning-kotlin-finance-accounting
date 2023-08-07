package local.learning.common.repo.expense

import local.learning.common.models.Error
import local.learning.common.models.expense.Expense

data class DbExpenseResponse(
    val expense: Expense?,
    val success: Boolean,
    val errors: List<Error> = emptyList()
)

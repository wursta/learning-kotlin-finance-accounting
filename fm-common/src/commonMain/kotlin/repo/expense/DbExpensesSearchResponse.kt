package local.learning.common.repo.expense

import local.learning.common.models.Error
import local.learning.common.models.expense.Expense

data class DbExpensesSearchResponse(
    val expenses: List<Expense> = emptyList(),
    val success: Boolean,
    val errors: List<Error> = emptyList()
)

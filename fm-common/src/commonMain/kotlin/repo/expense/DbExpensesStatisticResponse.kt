package local.learning.common.repo.expense

import local.learning.common.models.Error
import local.learning.common.models.expense.ExpenseStatistic

data class DbExpensesStatisticResponse(
    val expenseStatistic: ExpenseStatistic,
    val success: Boolean,
    val errors: List<Error> = emptyList()
)

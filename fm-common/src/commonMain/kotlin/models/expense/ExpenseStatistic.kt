package local.learning.common.models.expense

data class ExpenseStatistic(
    var total: Float = 0F,
    var summaryByCategory: List<ExpenseSummaryByCategory> = mutableListOf()
)
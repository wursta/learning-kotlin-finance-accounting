package local.learning.common.models.expense

import java.math.BigDecimal

data class ExpenseStatistic(
    var total: BigDecimal = BigDecimal.ZERO,
    var summaryByCategory: List<ExpenseSummaryByCategory> = mutableListOf()
)
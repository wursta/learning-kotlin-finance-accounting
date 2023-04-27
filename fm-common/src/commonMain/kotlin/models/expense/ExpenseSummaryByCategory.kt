package local.learning.common.models.expense

import local.learning.common.models.category.Category

data class ExpenseSummaryByCategory(
    var category: Category = Category(),
    var amount: Float = 0F
)
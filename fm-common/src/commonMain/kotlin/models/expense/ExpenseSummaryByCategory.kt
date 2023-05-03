package local.learning.common.models.expense

import local.learning.common.models.category.Category
import java.math.BigDecimal

data class ExpenseSummaryByCategory(
    var category: Category = Category(),
    var amount: BigDecimal = BigDecimal.ZERO
)
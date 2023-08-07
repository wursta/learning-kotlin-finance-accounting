package local.learning.common.repo.expense

import kotlinx.datetime.Instant

data class DbExpenseStatisticRequest(
    val dateFrom: Instant?,
    val dateTo: Instant?
)

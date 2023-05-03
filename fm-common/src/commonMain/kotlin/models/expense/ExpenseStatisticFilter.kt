package local.learning.common.models.expense

import kotlinx.datetime.Instant
import local.learning.common.INSTANT_NEGATIVE_INFINITY
import local.learning.common.INSTANT_POSITIVE_INFINITY

data class ExpenseStatisticFilter(
    var dateFrom: Instant = INSTANT_NEGATIVE_INFINITY,
    var dateTo: Instant = INSTANT_POSITIVE_INFINITY
)
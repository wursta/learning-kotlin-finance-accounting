package local.learning.common.models.expense

import kotlinx.datetime.Instant
import local.learning.common.INSTANT_NEGATIVE_INFINITY
import local.learning.common.INSTANT_POSITIVE_INFINITY
import local.learning.common.models.card.CardGuid

data class ExpenseSearchFilter(
    var amountFrom: Float = Float.NEGATIVE_INFINITY,
    var amountTo: Float = Float.POSITIVE_INFINITY,
    var dateFrom: Instant = INSTANT_NEGATIVE_INFINITY,
    var dateTo: Instant = INSTANT_POSITIVE_INFINITY,
    var sources: List<CardGuid> = mutableListOf()
)
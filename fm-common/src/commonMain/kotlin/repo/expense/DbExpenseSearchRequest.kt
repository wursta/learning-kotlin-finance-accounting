package local.learning.common.repo.expense

import kotlinx.datetime.Instant
import local.learning.common.models.card.CardGuid
import java.math.BigDecimal

data class DbExpenseSearchRequest(
    val amountFrom: BigDecimal?,
    val amountTo: BigDecimal?,
    val dateFrom: Instant?,
    val dateTo: Instant?,
    val cards: List<CardGuid> = emptyList(),
)

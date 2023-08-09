package local.learning.common.repo.expense

import kotlinx.datetime.Instant
import local.learning.common.models.PrincipalId

data class DbExpenseStatisticRequest(
    val createdBy: PrincipalId?,
    val dateFrom: Instant?,
    val dateTo: Instant?
)

package local.learning.common.models.expense

import kotlinx.datetime.Instant
import local.learning.common.INSTANT_NONE
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid

data class Expense(
    var guid: ExpenseGuid = ExpenseGuid.NONE,
    var createDT: Instant = INSTANT_NONE,
    var amount: Float = 0F,
    var cardGuid: CardGuid = CardGuid.NONE,
    var categoryGuid: CategoryGuid = CategoryGuid.NONE
)
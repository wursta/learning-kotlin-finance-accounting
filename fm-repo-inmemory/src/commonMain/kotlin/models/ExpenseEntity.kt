package local.learning.repo.inmemory.models

import kotlinx.datetime.Instant
import local.learning.common.INSTANT_NONE
import local.learning.common.models.LockGuid
import local.learning.common.models.PrincipalId
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseGuid
import java.math.BigDecimal

data class ExpenseEntity(
    var guid: String? = null,
    var createDT: Instant? = null,
    var amount: BigDecimal? = null,
    var cardGuid: String? = null,
    var categoryGuid: String? = null,
    var lockGuid: String? = null,
    var createdBy: String? = null
) {
    constructor(model: Expense) : this(
        guid = model.guid.asString().takeIf { it.isNotBlank() },
        createDT = model.createDT.takeIf { it != INSTANT_NONE },
        amount = model.amount.takeIf { it != BigDecimal.ZERO },
        cardGuid = model.cardGuid.asString().takeIf { it.isNotBlank() },
        categoryGuid = model.categoryGuid.asString().takeIf { it.isNotBlank() },
        lockGuid = model.lockGuid.asString().takeIf { it.isNotBlank() },
        createdBy = model.createdBy.takeIf { it != PrincipalId.NONE }?.asString(),
    )

    fun toInternal() = Expense(
        guid = guid?.let { ExpenseGuid(it) } ?: ExpenseGuid.NONE,
        createDT = createDT?: INSTANT_NONE,
        amount = amount ?: BigDecimal.ZERO,
        cardGuid = cardGuid?.let { CardGuid(it) } ?: CardGuid.NONE,
        categoryGuid = categoryGuid?.let { CategoryGuid(it) } ?: CategoryGuid.NONE,
        lockGuid = lockGuid?.let { LockGuid(it) } ?: LockGuid.NONE,
        createdBy = createdBy?.let { PrincipalId(it) } ?: PrincipalId.NONE

    )
}

package local.learning.repo.inmemory.models

import local.learning.common.models.LockGuid
import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardGuid

data class CardEntity(
    var guid: String? = null,
    var number: String? = null,
    var validFor: String? = null,
    var owner: String? = null,
    var bankGuid: String? = null,
    var lockGuid: String? = null,
) {
    constructor(model: Card): this(
        guid = model.guid.asString().takeIf { it.isNotBlank() },
        number = model.number.takeIf { it.isNotBlank() },
        validFor = model.validFor.takeIf { it.isNotBlank() },
        owner = model.owner.takeIf { it.isNotBlank() },
        bankGuid = model.bankGuid.asString().takeIf { it.isNotBlank() },
        lockGuid = model.lockGuid.asString().takeIf { it.isNotBlank() },
    )

    fun toInternal() = Card(
        guid = guid?.let { CardGuid(it) }?: CardGuid.NONE,
        number = number?: "",
        validFor = validFor?: "",
        owner = owner?: "",
        bankGuid = bankGuid?.let { BankGuid(it) }?: BankGuid.NONE,
        lockGuid = lockGuid?.let { LockGuid(it) }?: LockGuid.NONE
    )
}

package local.learning.common.models.card

import local.learning.common.models.LockGuid
import local.learning.common.models.PrincipalId
import local.learning.common.models.bank.BankGuid

data class Card(
    var guid: CardGuid = CardGuid.NONE,
    var number: String = "",
    var validFor: String = "",
    var owner: String = "",
    var bankGuid: BankGuid = BankGuid.NONE,
    var lockGuid: LockGuid = LockGuid.NONE,
    var createdBy: PrincipalId = PrincipalId.NONE,
    var principalRelations: Set<CardRelation> = emptySet()
)
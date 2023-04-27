package local.learning.common.models.card

import local.learning.common.models.bank.BankGuid

data class Card(
    var guid: CardGuid = CardGuid.NONE,
    var number: String = "",
    var validFor: String = "",
    var owner: String = "",
    var bankGuid: BankGuid = BankGuid.NONE
)
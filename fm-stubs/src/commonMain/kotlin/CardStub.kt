package local.learning.stubs

import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardGuid

object CardStub {
    fun get(): Card = Card(
        guid = CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
        number = "6915542779916368",
        validFor = "2025-12",
        owner = "SAZONOV MIKHAIL",
        bankGuid = BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
    )
}

package local.learning.common.models.card

data class Card(
    var guid: CardGuid = CardGuid.NONE,
    var number: String = "",
    var validFor: String = "",
    var owner: String = ""
)
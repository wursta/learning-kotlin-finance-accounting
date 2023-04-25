package local.learning.common.models.card

import kotlinx.datetime.Instant
import local.learning.common.INSTANT_NONE
import local.learning.common.models.Error
import local.learning.common.models.RequestId

data class CardContext(
    // Context fields
    var command: CardCommand = CardCommand.NONE,
    val errors: MutableList<Error> = mutableListOf(),

    // Request fields
    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = INSTANT_NONE,

    // Card fields
    var cardRequest: Card = Card(),
    var cardResponse: Card = Card(),
)
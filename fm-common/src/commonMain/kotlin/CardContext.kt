package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.State
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand

data class CardContext(
    // Context
    var command: CardCommand = CardCommand.NONE,
    var state: State = State.NONE,
    val errors: MutableList<Error> = mutableListOf(),

    // Request
    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = INSTANT_NONE,

    // Sources
    var cardRequest: Card = Card(),
    var cardResponse: Card = Card(),
)

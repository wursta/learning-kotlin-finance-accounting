package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.State
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand

data class CardContext(
    // Request
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = INSTANT_NONE,
    override var state: State = State.NONE,
    override val errors: MutableList<Error> = mutableListOf(),

    var command: CardCommand = CardCommand.NONE,

    // Requests & Responses
    var cardRequest: Card = Card(),
    var cardResponse: Card = Card(),
): IContext

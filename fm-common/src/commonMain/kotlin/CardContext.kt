package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.State
import local.learning.common.models.WorkMode
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardStubCase
import local.learning.common.repo.ICardRepository

data class CardContext(
    // Request
    override var requestId: RequestId = RequestId .NONE,
    override var timeStart: Instant = INSTANT_NONE,
    override var state: State = State.NONE,
    override var workMode: WorkMode = WorkMode.PROD,
    override val errors: MutableList<Error> = mutableListOf(),

    var stubCase: CardStubCase = CardStubCase.NONE,
    var command: CardCommand = CardCommand.NONE,

    //COR settings
    var corSettings: CorSettings = CorSettings.NONE,

    // Repo
    var repo: ICardRepository = ICardRepository.NONE,

    // Requests & Responses
    var cardRequest: Card = Card(),
    var cardResponse: Card = Card(),

    // Validation results
    var cardValidating: Card = Card(),

    // Repo results
    var cardRepoResult: Card = Card()
): IContext
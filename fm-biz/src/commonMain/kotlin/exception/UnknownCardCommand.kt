package local.learning.app.biz.exception

import local.learning.common.models.card.CardCommand

class UnknownCardCommand(command: CardCommand): Throwable("Unknown card command $command")
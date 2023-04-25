package local.learning.mappers.v1.exceptions

import local.learning.common.models.card.CardCommand

class UnknownCardCommand(command: CardCommand) : Throwable("Wrong command $command at mapping toTransport stage")
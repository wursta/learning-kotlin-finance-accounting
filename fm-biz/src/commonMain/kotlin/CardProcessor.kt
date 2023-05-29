package local.learning.app.biz

import local.learning.app.biz.exception.UnexpectedContext
import local.learning.app.biz.exception.UnknownCardCommand
import local.learning.common.CardContext
import local.learning.common.IContext
import local.learning.common.models.card.CardCommand
import local.learning.stubs.CardStub

class CardProcessor: IProcessor {
    override fun exec(ctx: IContext) {
        if (ctx !is CardContext) {
            throw UnexpectedContext()
        }
        when (ctx.command) {
            CardCommand.CREATE -> {
                ctx.cardResponse = CardStub.get()
            }
            CardCommand.READ -> {
                ctx.cardResponse = CardStub.get()
            }
            CardCommand.UPDATE -> {
                ctx.cardResponse = CardStub.get()
            }
            CardCommand.DELETE -> {
                ctx.cardResponse = CardStub.get()
            }
            else -> throw UnknownCardCommand(ctx.command)
        }
    }
}
package local.learning.app.biz.groups.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.common.CardContext
import local.learning.common.models.State
import local.learning.common.models.card.CardCommand

fun CorChainDsl<CardContext>.operation(
    title: String,
    command: CardCommand,
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
package local.learning.app.biz.groups.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.common.CardContext
import local.learning.common.models.State
import local.learning.common.models.WorkMode

@DslMarker
annotation class CardAccessDsl
fun CorChainDsl<CardContext>.access(
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = "Доступ"
    on { workMode != WorkMode.STUB && state == State.RUNNING }
}
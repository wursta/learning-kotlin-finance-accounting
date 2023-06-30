package local.learning.app.biz.groups.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.common.CardContext
import local.learning.common.models.State
import local.learning.common.models.WorkMode

@DslMarker
annotation class CardValidationDsl
fun CorChainDsl<CardContext>.validation(
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = "Валидация"
    on { workMode != WorkMode.STUB && state == State.RUNNING }
}
package local.learning.app.biz.workers.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.common.CardContext
import local.learning.common.models.State

fun CorChainDsl<CardContext>.init(title: String) = worker {
    this.title = title
    on { state == State.NONE }
    handle { state = State.RUNNING }
}

fun CorChainDsl<CardContext>.prepareResponse() = worker {
    this.title = "Подготовка ответа"
    on { state == State.RUNNING }
    handle {
        cardResponse = cardRepoResult.copy()
        state = State.FINISHING
    }
}
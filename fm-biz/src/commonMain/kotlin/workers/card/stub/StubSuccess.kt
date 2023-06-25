package local.learning.app.biz.workers.card.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.card.CardStubsDsl
import local.learning.common.CardContext
import local.learning.common.models.State
import local.learning.common.models.card.CardStubCase
import local.learning.stubs.CardStub

@CardStubsDsl
fun CorChainDsl<CardContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        cardResponse = CardStub.get()
    }
}

@CardStubsDsl
fun CorChainDsl<CardContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        cardResponse = CardStub.get()
    }
}

@CardStubsDsl
fun CorChainDsl<CardContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        cardResponse = CardStub.get()
    }
}

@CardStubsDsl
fun CorChainDsl<CardContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        cardResponse = CardStub.get()
    }
}
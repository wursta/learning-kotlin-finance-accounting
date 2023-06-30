package local.learning.app.biz.workers.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.common.ExpenseContext
import local.learning.common.models.State

fun CorChainDsl<ExpenseContext>.init(title: String) = worker {
    this.title = title
    on { state == State.NONE }
    handle { state = State.RUNNING }
}
package local.learning.app.biz.groups.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.common.ExpenseContext
import local.learning.common.models.State
import local.learning.common.models.WorkMode

@DslMarker
annotation class ExpenseAccessDsl
fun CorChainDsl<ExpenseContext>.access(
    block: CorChainDsl<ExpenseContext>.() -> Unit
) = chain {
    block()
    this.title = "Доступ"
    on { workMode != WorkMode.STUB && state == State.RUNNING }
}
package local.learning.app.biz.groups.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.common.ExpenseContext
import local.learning.common.models.State
import local.learning.common.models.WorkMode

@DslMarker
annotation class ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validation(
    block: CorChainDsl<ExpenseContext>.() -> Unit
) = chain {
    block()
    this.title = "Валидация"
    on { workMode != WorkMode.STUB && state == State.RUNNING }
}
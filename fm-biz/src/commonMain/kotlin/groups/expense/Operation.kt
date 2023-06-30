package local.learning.app.biz.groups.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.common.ExpenseContext
import local.learning.common.models.State
import local.learning.common.models.expense.ExpenseCommand

fun CorChainDsl<ExpenseContext>.operation(
    title: String,
    command: ExpenseCommand,
    block: CorChainDsl<ExpenseContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
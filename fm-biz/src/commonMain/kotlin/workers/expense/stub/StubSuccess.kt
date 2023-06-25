package local.learning.app.biz.workers.expense.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.card.CardStubsDsl
import local.learning.common.ExpenseContext
import local.learning.common.models.State
import local.learning.common.models.expense.ExpenseStubCase
import local.learning.stubs.ExpenseStub

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubSingleExpenseSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        expenseResponse = ExpenseStub.get()
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubListExpenseSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        expenseSearchResponse.addAll(ExpenseStub.getList())
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubExpenseStatisticSuccess(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        expenseStatisticResponse = ExpenseStub.getStatistic()
    }
}
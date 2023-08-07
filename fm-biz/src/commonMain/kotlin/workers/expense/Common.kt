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

fun CorChainDsl<ExpenseContext>.prepareExpenseResult() = worker {
    this.title = "Подготовка ответа"
    on { state == State.RUNNING }
    handle {
        expenseResponse = expenseRepoResult.copy()
        state = State.FINISHING
    }
}

fun CorChainDsl<ExpenseContext>.prepareSearchResult() = worker {
    this.title = "Подготовка ответа на поиск"
    on { state == State.RUNNING }
    handle {
        expenseSearchResponse = expenseSearchRepoResult.toMutableList()
        state = State.FINISHING
    }
}

fun CorChainDsl<ExpenseContext>.prepareStatisticResult() = worker {
    this.title = "Подготовка ответа на статистику"
    on { state == State.RUNNING }
    handle {
        expenseStatisticResponse = expenseStatisticRepoResult.copy()
        state = State.FINISHING
    }
}
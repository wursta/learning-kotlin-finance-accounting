package local.learning.app.biz.workers.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.common.ExpenseContext
import local.learning.common.INSTANT_NEGATIVE_INFINITY
import local.learning.common.INSTANT_POSITIVE_INFINITY
import local.learning.common.models.State
import local.learning.common.models.WorkMode
import local.learning.common.repo.IExpenseRepository
import local.learning.common.repo.expense.DbExpenseGuidRequest
import local.learning.common.repo.expense.DbExpenseRequest
import local.learning.common.repo.expense.DbExpenseSearchRequest
import local.learning.common.repo.expense.DbExpenseStatisticRequest
import java.math.BigDecimal

fun CorChainDsl<ExpenseContext>.initRepo() = worker {
    this.title = "Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы"
    on { state == State.RUNNING }
    handle {
        if (workMode == WorkMode.TEST) {
            repo = corSettings.expenseRepoTest
        } else if (workMode == WorkMode.PROD) {
            repo = corSettings.expenseRepoProd
        }
        if (workMode != WorkMode.STUB && repo == IExpenseRepository.NONE) {
            throw Exception("The database is unconfigured for chosen work mode [$workMode]")
        }
    }
}

fun CorChainDsl<ExpenseContext>.repoCreate() = worker {
    this.title = "Добавление новой траты"
    on { state == State.RUNNING }
    handle {
        val request = DbExpenseRequest(expenseValidating.copy())
        val result = repo.create(request)
        val resultExpense = result.expense
        if (result.success && resultExpense != null) {
            expenseRepoResult = resultExpense
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}

fun CorChainDsl<ExpenseContext>.repoRead() = worker {
    this.title = "Получение траты"
    on { state == State.RUNNING }
    handle {
        val request = DbExpenseGuidRequest(
            guid = expenseValidating.guid,
            lockGuid = expenseValidating.lockGuid
        )
        val result = repo.read(request)
        val resultExpense = result.expense
        if (result.success && resultExpense != null) {
            expenseRepoResult = resultExpense
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}

fun CorChainDsl<ExpenseContext>.repoUpdate() = worker {
    this.title = "Обновление траты"
    on { state == State.RUNNING }
    handle {
        val request = DbExpenseRequest(expenseValidating.copy())
        val result = repo.update(request)
        val resultExpense = result.expense
        if (result.success && resultExpense != null) {
            expenseRepoResult = resultExpense
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}

fun CorChainDsl<ExpenseContext>.repoDelete() = worker {
    this.title = "Удаление траты"
    on { state == State.RUNNING }
    handle {
        val request = DbExpenseGuidRequest(
            guid = expenseValidating.guid,
            lockGuid = expenseValidating.lockGuid
        )
        val result = repo.delete(request)
        val resultExpense = result.expense
        if (result.success && resultExpense != null) {
            expenseRepoResult = resultExpense
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}

fun CorChainDsl<ExpenseContext>.repoSearch() = worker {
    this.title = "Поиск трат"
    on { state == State.RUNNING }
    handle {
        val request = DbExpenseSearchRequest(
            amountFrom = expenseSearchValidating.amountFrom.takeIf { it != BigDecimal(-1) },
            amountTo = expenseSearchValidating.amountTo.takeIf { it != BigDecimal(-1) },
            dateFrom = expenseSearchValidating.dateFrom.takeIf { it != INSTANT_NEGATIVE_INFINITY },
            dateTo = expenseSearchValidating.dateTo.takeIf { it != INSTANT_POSITIVE_INFINITY },
            cards = expenseSearchValidating.sources.toList()
        )
        val result = repo.search(request)
        val resultExpenses = result.expenses
        if (result.success) {
            expenseSearchRepoResult = resultExpenses
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}

fun CorChainDsl<ExpenseContext>.repoStatistic() = worker {
    this.title = "Поиск трат"
    on { state == State.RUNNING }
    handle {
        val request = DbExpenseStatisticRequest(
            dateFrom = expenseStatisticValidating.dateFrom.takeIf { it != INSTANT_NEGATIVE_INFINITY },
            dateTo = expenseStatisticValidating.dateFrom.takeIf { it != INSTANT_POSITIVE_INFINITY }
        )
        val result = repo.stats(request)
        if (result.success) {
            expenseStatisticRepoResult = result.expenseStatistic
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}
package local.learning.app.biz.groups.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.app.biz.workers.expense.stub.*
import local.learning.common.ExpenseContext
import local.learning.common.models.State
import local.learning.common.models.WorkMode

@DslMarker
annotation class ExpenseStubsDsl

private fun ExpenseContext.stubsOnCallback() = workMode == WorkMode.STUB && state == State.RUNNING
@ExpenseStubsDsl
fun CorChainDsl<ExpenseContext>.stubCreate(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubSingleExpenseSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadAmount("Имитация ошибки валидации суммы")
    stubValidationBadCardGuid("Имитация ошибки валидации guid карты")
    stubValidationBadCategoryGuid("Имитация ошибки валидации guid категории")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}

@ExpenseStubsDsl
fun CorChainDsl<ExpenseContext>.stubRead(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubSingleExpenseSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadGuid("Имитация ошибки валидации guid")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}

@ExpenseStubsDsl
fun CorChainDsl<ExpenseContext>.stubUpdate(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubSingleExpenseSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadGuid("Имитация ошибки валидации guid")
    stubValidationBadAmount("Имитация ошибки валидации суммы")
    stubValidationBadCardGuid("Имитация ошибки валидации guid карты")
    stubValidationBadCategoryGuid("Имитация ошибки валидации guid категории")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}

@ExpenseStubsDsl
fun CorChainDsl<ExpenseContext>.stubDelete(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubSingleExpenseSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadGuid("Имитация ошибки валидации guid")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}
@ExpenseStubsDsl
fun CorChainDsl<ExpenseContext>.stubSearch(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubListExpenseSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadSearchFilterAmountFrom("Имитация ошибки валидации фильтра поиска amount_from")
    stubValidationBadSearchFilterAmountTo("Имитация ошибки валидации фильтра поиска amount_to")
    stubValidationBadSearchFilterSources("Имитация ошибки валидации фильтра поиска sources")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}

@ExpenseStubsDsl
fun CorChainDsl<ExpenseContext>.stubStatistic(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubExpenseStatisticSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadStatisticFilterDateFrom("Имитация ошибки валидации фильтра статистика date_from")
    stubValidationBadStatisticFilterDateTo("Имитация ошибки валидации фильтра статистика date_to")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}
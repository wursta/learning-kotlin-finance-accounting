package local.learning.app.biz.workers.expense.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.card.CardStubsDsl
import local.learning.common.ExpenseContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.helpers.addError
import local.learning.common.helpers.fail
import local.learning.common.models.Error
import local.learning.common.models.State
import local.learning.common.models.expense.ExpenseStubCase

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubNoCase(title: String) = worker {
    this.title = title
    // Возможны 2 ситуации отработки этого воркера:
    // 1. Нам приходит несуществующий stubCase и он превращается в NONE.
    // 2. State не поменялся на FINISHING или FAILING из-за того, что данный stubCase есть в словаре,
    // но он просто не обрабатывается в бизнес-логике.
    on { stubCase == ExpenseStubCase.NONE || state == State.RUNNING }
    handle {
        state = State.FAILING
        addError(
            Error(
                code = ErrorCode.INVALID_STUB_CASE,
                group = ErrorGroup.VALIDATION,
                field = "stubCase",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadGuid(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_GUID && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("guid", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}
@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadAmount(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_AMOUNT && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("amount", "Float > 0"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadCardGuid(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_CARD_GUID && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("card", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadCategoryGuid(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_CATEGORY_GUID && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("category", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadSearchFilterAmountFrom(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_AMOUNT_FROM && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("amount_from", "null of (Float > 0 and < amount_to)"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadSearchFilterAmountTo(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_AMOUNT_TO && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("amount_to", "null or (Float > 0 and > amount_from)"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadSearchFilterSources(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_SEARCH_FILTER_SOURCES && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("sources", "[\"1598044e-5259-11e9-8647-d663bd873d93\", ...]"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadStatisticFilterDateFrom(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_STATISTIC_FILTER_DATE_FROM && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("date_from", "2023-06-25T10:12:22.722Z"))
    }
}

@CardStubsDsl
fun CorChainDsl<ExpenseContext>.stubValidationBadStatisticFilterDateTo(title: String) = worker {
    this.title = title
    on { stubCase == ExpenseStubCase.VALIDATION_ERROR_BAD_STATISTIC_FILTER_DATE_TO && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("date_to", "2023-06-25T10:12:22.722Z"))
    }
}
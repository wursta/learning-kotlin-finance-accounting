package local.learning.app.biz.workers.expense

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.expense.ExpenseValidationDsl
import local.learning.common.ExpenseContext
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.helpers.*
import local.learning.common.models.State

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateGuid(title: String) = worker {
    this.title = title
    on { !expenseValidating.isGuidValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("guid", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateAmount(title: String) = worker {
    this.title = title
    on { !expenseValidating.isAmountValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("amount", "Float > 0"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateCardGuid(title: String) = worker {
    this.title = title
    on { !expenseValidating.isCardGuidValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("card", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateCategoryGuid(title: String) = worker {
    this.title = title
    on { !expenseValidating.isCategoryGuidValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("category", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateSearchFilterAmountFrom(title: String) = worker {
    this.title = title
    on { !expenseSearchValidating.isAmountFromValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("amount_from", "null or (Float > 0 and < amount_to)"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateSearchFilterAmountTo(title: String) = worker {
    this.title = title
    on { !expenseSearchValidating.isAmountToValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("amount_to", "null or (Float > 0 and > amount_from)"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateSearchFilterDateFrom(title: String) = worker {
    this.title = title
    on { !expenseSearchValidating.isDatesValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("date_from", "null or < date_to"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateSearchFilterDateTo(title: String) = worker {
    this.title = title
    on { !expenseSearchValidating.isDatesValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("date_to", "null or > date_from"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateSearchFilterSources(title: String) = worker {
    this.title = title
    on { !expenseSearchValidating.isSourcesValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("sources", "[\"1598044e-5259-11e9-8647-d663bd873d93\", ...]"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateStatisticFilterDateFrom(title: String) = worker {
    this.title = title
    on { !expenseStatisticValidating.isDatesValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("date_from", "null or < date_to"))
    }
}

@ExpenseValidationDsl
fun CorChainDsl<ExpenseContext>.validateStatisticFilterDateTo(title: String) = worker {
    this.title = title
    on { !expenseStatisticValidating.isDatesValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("date_to", "null or > date_from"))
    }
}
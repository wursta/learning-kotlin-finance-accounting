package local.learning.app.biz.workers.card.stub

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.card.CardStubsDsl
import local.learning.common.CardContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.helpers.addError
import local.learning.common.helpers.fail
import local.learning.common.models.Error
import local.learning.common.models.State
import local.learning.common.models.card.CardStubCase

@CardStubsDsl
fun CorChainDsl<CardContext>.stubNoCase(title: String) = worker {
    this.title = title
    // Возможны 2 ситуации отработки этого воркера:
    // 1. Нам приходит несуществующий stubCase и он превращается в NONE.
    // 2. State не поменялся на FINISHING или FAILING из-за того, что данный stubCase есть в словаре,
    // но он просто не обрабатывается в бизнес-логике.
    on { stubCase == CardStubCase.NONE || state == State.RUNNING }
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
fun CorChainDsl<CardContext>.stubValidationBadGuid(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.VALIDATION_ERROR_BAD_GUID && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("guid", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}
@CardStubsDsl
fun CorChainDsl<CardContext>.stubValidationBadNumber(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.VALIDATION_ERROR_BAD_NUMBER && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("number", "0000000000000000"))
    }
}

@CardStubsDsl
fun CorChainDsl<CardContext>.stubValidationBadValidFor(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.VALIDATION_ERROR_BAD_VALID_FOR && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("valid_for", "2023-01"))
    }
}

@CardStubsDsl
fun CorChainDsl<CardContext>.stubValidationBadOwner(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.VALIDATION_ERROR_BAD_OWNER && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("owner", "string from 3 to 50 chars"))
    }
}

@CardStubsDsl
fun CorChainDsl<CardContext>.stubValidationBadBankGuid(title: String) = worker {
    this.title = title
    on { stubCase == CardStubCase.VALIDATION_ERROR_BAD_BANK_GUID && state == State.RUNNING }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("bank_guid", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}
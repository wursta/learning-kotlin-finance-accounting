package local.learning.app.biz.workers.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import local.learning.app.biz.groups.card.CardValidationDsl
import local.learning.common.CardContext
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.helpers.*
import local.learning.common.models.State

@CardValidationDsl
fun CorChainDsl<CardContext>.validateGuid(title: String) = worker {
    this.title = title
    on { !cardValidating.isGuidValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("guid", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}
@CardValidationDsl
fun CorChainDsl<CardContext>.validateNumberFormat(title: String) = worker {
    this.title = title
    on { !cardValidating.isNumberValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("number", "0000000000000000"))
    }
}

@CardValidationDsl
fun CorChainDsl<CardContext>.validateValidFor(title: String) = worker {
    this.title = title
    on { !cardValidating.isValidForValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("valid_for", "2023-01"))
    }
}

@CardValidationDsl
fun CorChainDsl<CardContext>.validateOwner(title: String) = worker {
    this.title = title
    on { !cardValidating.isOwnerValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("owner", "string from 3 to 50 chars"))
    }
}

@CardValidationDsl
fun CorChainDsl<CardContext>.validateBankGuid(title: String) = worker {
    this.title = title
    on { !cardValidating.isBankGuidValid() }
    handle {
        state = State.FAILING
        fail(InvalidFieldFormat("bankGuid", "1598044e-5259-11e9-8647-d663bd873d93"))
    }
}
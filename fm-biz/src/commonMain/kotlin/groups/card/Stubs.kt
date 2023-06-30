package local.learning.app.biz.groups.card

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import local.learning.app.biz.workers.card.stub.*
import local.learning.common.CardContext
import local.learning.common.models.State
import local.learning.common.models.WorkMode

@DslMarker
annotation class CardStubsDsl

private fun CardContext.stubsOnCallback() = workMode == WorkMode.STUB && state == State.RUNNING
@CardStubsDsl
fun CorChainDsl<CardContext>.stubCreate(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubCreateSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadNumber("Имитация ошибки валидации номера карты")
    stubValidationBadValidFor("Имитация ошибки валидации срока действия")
    stubValidationBadOwner("Имитация ошибки валидации имени владельца")
    stubValidationBadBankGuid("Имитация ошибки валидации guid банка")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}
@CardStubsDsl
fun CorChainDsl<CardContext>.stubRead(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubReadSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadGuid("Имитация ошибки валидации guid карты")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}
@CardStubsDsl
fun CorChainDsl<CardContext>.stubUpdate(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubUpdateSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadGuid("Имитация ошибки валидации guid карты")
    stubValidationBadNumber("Имитация ошибки валидации номера карты")
    stubValidationBadValidFor("Имитация ошибки валидации срока действия")
    stubValidationBadOwner("Имитация ошибки валидации имени владельца")
    stubValidationBadBankGuid("Имитация ошибки валидации guid банка")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}
@CardStubsDsl
fun CorChainDsl<CardContext>.stubDelete(
    title: String
) = chain {
    this.title = title
    on { stubsOnCallback() }

    // Success
    stubDeleteSuccess("Имитация успешной обработки")

    // Validation errors
    stubValidationBadGuid("Имитация ошибки валидации guid карты")

    // Exceptions
    stubNoCase("Ошибка: запрошенный стаб недопустим")
}
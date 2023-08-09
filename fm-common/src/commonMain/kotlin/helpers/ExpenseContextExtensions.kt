package local.learning.common.helpers

import local.learning.common.ExpenseContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.models.Error
import local.learning.common.models.State

fun ExpenseContext.addError(vararg error: Error)  = errors.addAll(error)
fun ExpenseContext.fail(e: Throwable) = addError(e.asError())
fun ExpenseContext.fail(e: InvalidFieldFormat) = addError(e.asError())
fun ExpenseContext.addFieldValidationError(field: String, description: String) = addError(
    Error(
        code = ErrorCode.INVALID_FIELD_FORMAT,
        group = ErrorGroup.VALIDATION,
        field = field,
        message = "Validation error for field $field. $description",
    )
)

fun ExpenseContext.repoFail(dbErrors: List<Error>) {
    state = State.FAILING
    errors.addAll(dbErrors)
}
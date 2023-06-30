package local.learning.common.helpers

import local.learning.common.CardContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.models.Error

fun CardContext.addError(vararg error: Error)  = errors.addAll(error)
fun CardContext.fail(e: Throwable) = addError(e.asError())
fun CardContext.fail(e: InvalidFieldFormat) = addError(e.asError())
fun CardContext.addFieldValidationError(field: String, description: String) = addError(
    Error(
        code = ErrorCode.INVALID_FIELD_FORMAT,
        group = ErrorGroup.VALIDATION,
        field = field,
        message = "Validation error for field $field. $description",
    )
)
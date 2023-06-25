package local.learning.common.helpers

import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.exceptions.InvalidFieldFormat
import local.learning.common.models.Error

fun InvalidFieldFormat.asError(
    code: ErrorCode = ErrorCode.INVALID_FIELD_FORMAT,
    group: ErrorGroup = ErrorGroup.VALIDATION,
    message: String = this.message ?: "",
) = Error(
    code = code,
    group = group,
    field = field,
    message = message,
    exception = this,
)

fun Throwable.asError(
    code: ErrorCode = ErrorCode.UNKNOWN,
    group: ErrorGroup = ErrorGroup.EXCEPTIONS,
    message: String = this.message ?: "",
) = Error(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
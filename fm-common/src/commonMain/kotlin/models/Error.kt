package local.learning.common.models

import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup

data class Error(
    val code: ErrorCode = ErrorCode.UNKNOWN,
    val group: ErrorGroup = ErrorGroup.UNKNOWN,
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
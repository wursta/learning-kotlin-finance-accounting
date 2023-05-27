package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.State

interface IContext {
    // Request
    var requestId: RequestId
    var timeStart: Instant
    var state: State
    val errors: MutableList<Error>
}
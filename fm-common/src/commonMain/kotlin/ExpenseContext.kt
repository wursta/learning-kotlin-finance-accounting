package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.State
import local.learning.common.models.expense.*

data class ExpenseContext(
    // Request
    override var state: State = State.NONE,
    override val errors: MutableList<Error> = mutableListOf(),
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = INSTANT_NONE,

    var command: ExpenseCommand = ExpenseCommand.NONE,

    // Requests & Responses
    var expenseRequest: Expense = Expense(),
    var expenseResponse: Expense = Expense(),
    var expenseSearchRequest: ExpenseSearchFilter = ExpenseSearchFilter(),
    var expenseSearchResponse: MutableList<Expense> = mutableListOf(),
    var expenseStatisticRequest: ExpenseStatisticFilter = ExpenseStatisticFilter(),
    var expenseStatisticResponse: ExpenseStatistic = ExpenseStatistic()
): IContext

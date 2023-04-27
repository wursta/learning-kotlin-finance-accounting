package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.Error
import local.learning.common.models.RequestId
import local.learning.common.models.State
import local.learning.common.models.expense.*

data class ExpenseContext(
    // Context
    var command: ExpenseCommand = ExpenseCommand.NONE,
    var state: State = State.NONE,
    val errors: MutableList<Error> = mutableListOf(),

    // Request
    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = INSTANT_NONE,

    // Expense
    var expenseRequest: Expense = Expense(),
    var expenseResponse: Expense = Expense(),
    var expenseSearchRequest: ExpenseSearchFilter = ExpenseSearchFilter(),
    var expenseSearchResponse: MutableList<Expense> = mutableListOf(),
    var expenseStatisticRequest: ExpenseStatisticFilter = ExpenseStatisticFilter(),
    var expenseStatisticResponse: ExpenseStatistic = ExpenseStatistic()
)

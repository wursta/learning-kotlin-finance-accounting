package local.learning.common

import kotlinx.datetime.Instant
import local.learning.common.models.*
import local.learning.common.models.expense.*
import local.learning.common.repo.IExpenseRepository

data class ExpenseContext(
    // Request
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = INSTANT_NONE,
    override var state: State = State.NONE,
    override var workMode: WorkMode = WorkMode.STUB,
    override val errors: MutableList<Error> = mutableListOf(),

    var command: ExpenseCommand = ExpenseCommand.NONE,
    var stubCase: ExpenseStubCase = ExpenseStubCase.NONE,

    //COR settings
    var corSettings: CorSettings = CorSettings.NONE,

    // Repo
    var repo: IExpenseRepository = IExpenseRepository.NONE,

    // Access
    var principal: Principal = Principal.NONE,
    val permissions: MutableSet<ExpensePermission> = mutableSetOf(),
    var permitted: Boolean = false,

    // Requests & Responses
    var expenseRequest: Expense = Expense(),
    var expenseResponse: Expense = Expense(),
    var expenseSearchRequest: ExpenseSearchFilter = ExpenseSearchFilter(),
    var expenseSearchResponse: MutableList<Expense> = mutableListOf(),
    var expenseStatisticRequest: ExpenseStatisticFilter = ExpenseStatisticFilter(),
    var expenseStatisticResponse: ExpenseStatistic = ExpenseStatistic(),

    // Validation
    var expenseValidating: Expense = Expense(),
    var expenseSearchValidating: ExpenseSearchFilter = ExpenseSearchFilter(),
    var expenseStatisticValidating: ExpenseStatisticFilter = ExpenseStatisticFilter(),

    // Repo results
    var expenseRepoResult: Expense = Expense(),
    var expenseSearchRepoResult: List<Expense> = listOf(),
    var expenseStatisticRepoResult: ExpenseStatistic = ExpenseStatistic()
) : IContext

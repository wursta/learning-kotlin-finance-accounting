package local.learning.app.biz

import local.learning.app.biz.exception.UnknownExpenseCommand
import local.learning.common.ExpenseContext
import local.learning.common.models.expense.ExpenseCommand
import local.learning.stubs.ExpenseStub

class ExpenseProcessor {
    fun exec(ctx: ExpenseContext) {
        when (ctx.command) {
            ExpenseCommand.CREATE -> {
                ctx.expenseResponse = ExpenseStub.get()
            }
            ExpenseCommand.READ -> {
                ctx.expenseResponse = ExpenseStub.get()
            }
            ExpenseCommand.UPDATE -> {
                ctx.expenseResponse = ExpenseStub.get()
            }
            ExpenseCommand.DELETE -> {
                ctx.expenseResponse = ExpenseStub.get()
            }
            ExpenseCommand.SEARCH -> {
                ctx.expenseSearchResponse.addAll(ExpenseStub.getList())
            }
            ExpenseCommand.STATS -> {
                ctx.expenseStatisticResponse = ExpenseStub.getStatistic()
            }
            else -> throw UnknownExpenseCommand(ctx.command)
        }
    }
}
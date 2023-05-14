package local.learning.app.biz.exception

import local.learning.common.models.expense.ExpenseCommand

class UnknownExpenseCommand(command: ExpenseCommand): Throwable("Unknown expense command $command")
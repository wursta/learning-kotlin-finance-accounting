package local.learning.mappers.exceptions

import local.learning.common.models.expense.ExpenseCommand

class UnknownExpenseCommand(command: ExpenseCommand) : Throwable("Wrong command $command at mapping toTransport stage")
package local.learning.common.repo.expense

import local.learning.common.models.LockGuid
import local.learning.common.models.expense.ExpenseGuid

data class DbExpenseGuidRequest(
    val guid: ExpenseGuid,
    val lockGuid: LockGuid = LockGuid.NONE
)

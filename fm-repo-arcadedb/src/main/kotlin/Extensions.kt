package local.learning.repo.arcadedb

import com.arcadedb.query.sql.executor.Result
import kotlinx.datetime.Instant
import local.learning.common.INSTANT_NONE
import local.learning.common.models.LockGuid
import local.learning.common.models.PrincipalId
import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseGuid
import java.math.BigDecimal

fun Result.toCardInternal(): Card = Card(
    guid = if (this.hasProperty("guid")) CardGuid(this.getProperty("guid")) else CardGuid.NONE,
    number = if (this.hasProperty("number")) this.getProperty("number") else "",
    validFor = if (this.hasProperty("validFor")) this.getProperty("validFor") else "",
    owner  = if (this.hasProperty("owner")) this.getProperty("owner") else "",
    bankGuid = if (this.hasProperty("owner")) BankGuid(this.getProperty("bankGuid")) else BankGuid.NONE,
    lockGuid = if (this.hasProperty("lockGuid")) LockGuid(this.getProperty("lockGuid")) else LockGuid.NONE,
    createdBy = if (this.hasProperty("createdBy")) PrincipalId(this.getProperty("createdBy")) else PrincipalId.NONE,
)

fun Result.toExpenseInternal(): Expense = Expense(
    guid = if (this.hasProperty("guid")) ExpenseGuid(this.getProperty("guid")) else ExpenseGuid.NONE,
    createDT = if (this.hasProperty("createDT")) Instant.fromEpochMilliseconds(this.getProperty("createDT")) else INSTANT_NONE,
    amount = if (this.hasProperty("amount")) BigDecimal(this.getProperty<Double>("amount")) else BigDecimal.ZERO,
    cardGuid = if (this.hasProperty("cardGuid")) CardGuid(this.getProperty("cardGuid")) else CardGuid.NONE,
    categoryGuid = if (this.hasProperty("categoryGuid")) CategoryGuid(this.getProperty("categoryGuid")) else CategoryGuid.NONE,
    lockGuid = if (this.hasProperty("lockGuid")) LockGuid(this.getProperty("lockGuid")) else LockGuid.NONE,
    createdBy = if (this.hasProperty("createdBy")) PrincipalId(this.getProperty("createdBy")) else PrincipalId.NONE
)
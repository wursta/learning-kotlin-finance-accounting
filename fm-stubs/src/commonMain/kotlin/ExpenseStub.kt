package local.learning.stubs

import kotlinx.datetime.Instant
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.Category
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseGuid
import local.learning.common.models.expense.ExpenseStatistic
import local.learning.common.models.expense.ExpenseSummaryByCategory
import java.math.BigDecimal

object ExpenseStub {
    fun get(): Expense = makeExpense(
        guid = ExpenseGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
        amount = BigDecimal(100),
        cardGuid = CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
        categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
    )

    fun getList(): List<Expense> = listOf(
        makeExpense(
            guid = ExpenseGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
            amount = BigDecimal(100),
            cardGuid = CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
            categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
        ),
        makeExpense(
            guid = ExpenseGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
            amount = BigDecimal(205.32),
            cardGuid = CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
            categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c")
        )
    )

    fun getStatistic(): ExpenseStatistic = ExpenseStatistic(
        total = BigDecimal(105304),
        summaryByCategory = listOf(
            ExpenseSummaryByCategory(
                category = Category(
                    guid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"),
                    name = "Кредит"
                ),
                amount = BigDecimal(20345)
            ),
            ExpenseSummaryByCategory(
                category = Category(
                    guid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d8a7164c"),
                    name = "Еда"
                ),
                amount = BigDecimal(30342.5)
            ),
            ExpenseSummaryByCategory(
                category = Category(
                    guid = CategoryGuid("5410bdaf-834a-4ga6-9044-ee25d8a7164c"),
                    name = "Одежда"
                ),
                amount = BigDecimal(54616.5)
            )
        )
    )
    private fun makeExpense(guid: ExpenseGuid, amount: BigDecimal, cardGuid: CardGuid, categoryGuid: CategoryGuid) = Expense(
        guid = guid,
        createDT = Instant.parse("2023-01-01T14:46:04Z"),
        amount = amount,
        cardGuid = cardGuid,
        categoryGuid = categoryGuid
    )
}

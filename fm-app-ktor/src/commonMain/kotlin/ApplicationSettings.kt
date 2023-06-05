package local.learning.app.ktor

import local.learning.app.biz.CardProcessor
import local.learning.app.biz.ExpenseProcessor
import local.learning.common.CorSettings

data class ApplicationSettings (
    val corSettings: CorSettings,
    val cardProcessor: CardProcessor,
    val expenseProcessor: ExpenseProcessor
)
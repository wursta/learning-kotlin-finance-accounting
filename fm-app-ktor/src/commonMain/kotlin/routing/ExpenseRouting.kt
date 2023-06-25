package local.learning.app.ktor.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import local.learning.api.models.*
import local.learning.app.ktor.ApplicationSettings
import local.learning.app.ktor.controller.expenseAction
import local.learning.common.models.expense.ExpenseCommand

fun Route.expense(appSettings: ApplicationSettings) {
    route("expense") {
        post("create") {
            call.expenseAction<ExpenseCreateRequestDto>(appSettings, ExpenseCommand.CREATE)
        }
        post("read") {
            call.expenseAction<ExpenseReadRequestDto>(appSettings, ExpenseCommand.READ)
        }
        post("update") {
            call.expenseAction<ExpenseUpdateRequestDto>(appSettings, ExpenseCommand.UPDATE)
        }
        post("delete") {
            call.expenseAction<ExpenseDeleteRequestDto>(appSettings, ExpenseCommand.DELETE)
        }
        post("search") {
            call.expenseAction<ExpenseSearchRequestDto>(appSettings, ExpenseCommand.SEARCH)
        }
        post("stats") {
            call.expenseAction<ExpenseStatsRequestDto>(appSettings, ExpenseCommand.STATS)
        }
    }
}
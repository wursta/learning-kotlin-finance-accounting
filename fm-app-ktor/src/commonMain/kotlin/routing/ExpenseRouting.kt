package local.learning.app.ktor.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import local.learning.api.models.*
import local.learning.app.ktor.ApplicationSettings
import local.learning.app.ktor.controller.expenseAction

fun Route.expense(appSettings: ApplicationSettings) {
    route("expense") {
        post("create") {
            call.expenseAction<ExpenseCreateRequestDto>(appSettings)
        }
        post("read") {
            call.expenseAction<ExpenseReadRequestDto>(appSettings)
        }
        post("update") {
            call.expenseAction<ExpenseUpdateRequestDto>(appSettings)
        }
        post("delete") {
            call.expenseAction<ExpenseDeleteRequestDto>(appSettings)
        }
        post("search") {
            call.expenseAction<ExpenseSearchRequestDto>(appSettings)
        }
        post("stats") {
            call.expenseAction<ExpenseStatsRequestDto>(appSettings)
        }
    }
}
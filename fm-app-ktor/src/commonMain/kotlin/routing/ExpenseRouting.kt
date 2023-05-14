package local.learning.app.ktor.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import local.learning.api.models.*
import local.learning.app.biz.ExpenseProcessor
import local.learning.app.ktor.controller.expenseAction

fun Route.expense(processor: ExpenseProcessor) {
    route("expense") {
        post("create") {
            call.expenseAction<ExpenseCreateRequestDto>(processor)
        }
        post("read") {
            call.expenseAction<ExpenseReadRequestDto>(processor)
        }
        post("update") {
            call.expenseAction<ExpenseUpdateRequestDto>(processor)
        }
        post("delete") {
            call.expenseAction<ExpenseDeleteRequestDto>(processor)
        }
        post("search") {
            call.expenseAction<ExpenseSearchRequestDto>(processor)
        }
        post("stats") {
            call.expenseAction<ExpenseStatsRequestDto>(processor)
        }
    }
}
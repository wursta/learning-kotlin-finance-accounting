package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseSeverityLevel

object ExpenseRoutes {
    const val create = "api/expense/create"
    const val read = "api/expense/read"
    const val update = "api/expense/update"
    const val delete = "api/expense/delete"
    const val search = "api/expense/search"
    const val stats = "api/expense/stats"
}
class ExpenseTest: BehaviorSpec({
    severity = TestCaseSeverityLevel.MINOR

    beforeEach {
        //clearDb()
    }

    given("Create new expense") {
        `when`("Fill correctly all required fields") {
            then("Expense saved") {
                TODO("Not realized")
            }
        }
        `when`("Fill sum, date, choose cash checkbox, choose category") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Outcome saved") {
                TODO("Not realized")
            }
        }
        `when`("Do not fill sum") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Fill not valid sum (negative, null, 0)") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Do not fill date") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Fill not valid date format") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Do not choose card from saved list and not check \"Cash\" checkbox") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Send non-existent category id") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
    }
})
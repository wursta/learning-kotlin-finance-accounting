package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseSeverityLevel

class CreateOutcomeRestAcceptance: BehaviorSpec({
    severity = TestCaseSeverityLevel.MINOR

    given("Add new outcome with custom bank card or cash") {
        `when`("Fill sum, date, choose card from added, choose category") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Outcome saved") {
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
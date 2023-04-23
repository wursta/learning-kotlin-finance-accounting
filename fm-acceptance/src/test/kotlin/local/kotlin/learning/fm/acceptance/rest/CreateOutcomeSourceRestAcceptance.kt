package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseSeverityLevel

class CreateOutcomeSourceRestAcceptance: BehaviorSpec({
    severity = TestCaseSeverityLevel.MINOR

    given("Add new bank card as source of income") {
        `when`("Fill valid card details") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Card saved") {
                TODO("Not realized")
            }
        }
        `when`("Do not fill card number") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Fill not valid card number") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Do not fill card valid date") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Fill card valid date with past date") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Do not fill card owner name") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
        `when`("Do not choose bank from list of available or not fill bank name if choose \"Other\" variant") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have validation error") {
                TODO("Not realized")
            }
        }
    }
})
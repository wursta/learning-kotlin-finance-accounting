package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseSeverityLevel

class StatisticRestAcceptance: BehaviorSpec({
    severity = TestCaseSeverityLevel.MINOR

    given("Get statistics by categories for period") {
        `when`("All categories and all dates") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Statistics match the data") {
                TODO("Not realized")
            }
        }
        `when`("Choose some categories and all dates") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Statistics match the data") {
                TODO("Not realized")
            }
        }
        `when`("Choose all categories and fill from-to dates") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Statistics match the data") {
                TODO("Not realized")
            }
        }
        `when`("Choose some categories and fill from-to dates") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("Statistics match the data") {
                TODO("Not realized")
            }
        }
        `when`("Send non-existent categories") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have error message in response") {
                TODO("Not realized")
            }
        }
        `when`("Send not valid dates format") {
            then("Response is 200") {
                TODO("Not realized")
            }
            then("I have error message in response") {
                TODO("Not realized")
            }
        }
    }
})
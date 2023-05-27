package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseSeverityLevel

object CardRoutes {
    const val create = "api/card/create"
    const val read = "api/card/read"
    const val update = "api/card/update"
    const val delete = "api/card/delete"
}
class CardTest: BehaviorSpec({
    severity = TestCaseSeverityLevel.MINOR

    beforeEach {
        //clearDb()
    }

    given("Create new bank card") {
        `when`("Fill correctly all required fields") {
            then("Card is saved") {
                TODO("Not realized")
            }
        }
//        `when`("Do not fill card number or number is invalid") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Do not fill card valid date") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Do not fill card owner name") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
    }

    given("Read card") {
        `when`("Get info about card with existed guid") {
            then("Card info retrieved") {
                TODO("Not realized")
            }
        }
//        `when`("Get info about non existed card") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
    }

    given("Update card") {
        `when`("Card guid existed, fill correctly all required fields") {
            then("Card is updated") {
                TODO("Not realized")
            }
        }
//        `when`("Try to update non existed card") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Do not fill card number or number is invalid") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Do not fill card valid date") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Do not fill card owner name") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
    }

    given("Delete card") {
        `when`("Delete card") {
            then("Card is deleted") {
                TODO("Not realized")
            }
        }
        `when`("Try to delete non existed card") {
            then("Get validation error") {
                TODO("Not realized")
            }
        }
    }
})
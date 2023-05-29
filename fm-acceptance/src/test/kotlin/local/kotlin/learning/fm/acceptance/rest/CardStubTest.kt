package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import local.kotlin.learning.fm.acceptance.RestClient
import local.kotlin.learning.fm.acceptance.stub.CardStub

class CardStubTest: BehaviorSpec({
    given("Create new bank card") {
        `when`("Fill correctly all required fields") {
            then("Card is saved") {
                val response = RestClient.request(CardRoutes.create, CardStub.createRequest)
                response shouldBe CardStub.createResponse
            }
        }
    }

    given("Read card") {
        `when`("Get info about card with existed guid") {
            then("Card info retrieved") {
                val response = RestClient.request(CardRoutes.read, CardStub.readRequest)
                response shouldBe CardStub.readResponse
            }
        }
    }

    given("Update card") {
        `when`("Card guid existed, fill correctly all required fields") {
            then("Card is updated") {
                val response = RestClient.request(CardRoutes.update, CardStub.updateRequest)
                response shouldBe CardStub.updateResponse
            }
        }
    }

    given("Delete card") {
        `when`("Delete card") {
            then("Card is deleted") {
                val response = RestClient.request(CardRoutes.delete, CardStub.deleteRequest)
                response shouldBe CardStub.deleteResponse
            }
        }
    }
})
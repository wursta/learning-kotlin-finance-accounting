package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import local.kotlin.learning.fm.acceptance.RestClient
import local.kotlin.learning.fm.acceptance.stub.ExpenseStub
import local.learning.api.models.*

class ExpenseStubTest: BehaviorSpec({
    given("Create new expense") {
        `when`("Fill correctly all required fields") {
            then("Expense saved") {
                val response = RestClient.request(ExpenseRoutes.create, ExpenseStub.createRequest)
                if (response !is ExpenseCreateResponseDto) {
                    error("response is not " + ExpenseCreateResponseDto::class)
                }
                response.expense shouldNotBe null
                response.expense?.guid shouldBe ExpenseStub.createResponse.expense?.guid
                response.expense?.amount shouldBe ExpenseStub.createResponse.expense?.amount
                response.expense?.card shouldBe ExpenseStub.createResponse.expense?.card
                response.expense?.category shouldBe ExpenseStub.createResponse.expense?.category
            }
        }
    }

    given("Read expense") {
        `when`("Get info about expense with existed guid") {
            then("Expense info retrieved") {
                val response = RestClient.request(ExpenseRoutes.read, ExpenseStub.readRequest)
                if (response !is ExpenseReadResponseDto) {
                    error("response is not " + ExpenseReadResponseDto::class)
                }
                response.expense shouldNotBe null
                response.expense?.guid shouldBe ExpenseStub.readResponse.expense?.guid
                response.expense?.amount shouldBe ExpenseStub.readResponse.expense?.amount
                response.expense?.card shouldBe ExpenseStub.readResponse.expense?.card
                response.expense?.category shouldBe ExpenseStub.readResponse.expense?.category
            }
        }
    }

    given("Update expense") {
        `when`("Expense guid existed, fill correctly all required fields") {
            then("Expense is updated") {
                val response = RestClient.request(ExpenseRoutes.update, ExpenseStub.updateRequest)
                if (response !is ExpenseUpdateResponseDto) {
                    error("response is not " + ExpenseUpdateResponseDto::class)
                }
                response.expense shouldNotBe null
                response.expense?.guid shouldBe ExpenseStub.updateResponse.expense?.guid
                response.expense?.amount shouldBe ExpenseStub.updateResponse.expense?.amount
                response.expense?.card shouldBe ExpenseStub.updateResponse.expense?.card
                response.expense?.category shouldBe ExpenseStub.updateResponse.expense?.category
            }
        }
    }

    given("Delete expense") {
        `when`("Delete expense") {
            then("Expense is deleted") {
                val response = RestClient.request(ExpenseRoutes.delete, ExpenseStub.deleteRequest)
                if (response !is ExpenseDeleteResponseDto) {
                    error("response is not " + ExpenseDeleteResponseDto::class)
                }
                response.expense shouldNotBe null
                response.expense?.guid shouldBe ExpenseStub.deleteResponse.expense?.guid
                response.expense?.amount shouldBe ExpenseStub.deleteResponse.expense?.amount
                response.expense?.card shouldBe ExpenseStub.deleteResponse.expense?.card
                response.expense?.category shouldBe ExpenseStub.deleteResponse.expense?.category
            }
        }
    }

    given("Search expense") {
        `when`("Search expense") {
            then("Get filtered expenses list") {
                val response = RestClient.request(ExpenseRoutes.search, ExpenseStub.searchRequest)
                if (response !is ExpenseSearchResponseDto) {
                    error("response is not " + ExpenseSearchResponseDto::class)
                }

                response.expenses shouldNotBe null
                response.expenses?.get(0) shouldNotBe null

                val firstExpense = response.expenses?.get(0) ?: error("response expense at index 0 is null")
                val stubExpense = ExpenseStub.searchResponse.expenses?.get(0) ?: error("stub expense at index 0 is null")

                firstExpense.guid shouldBe stubExpense.guid
                firstExpense.amount shouldBe stubExpense.amount
                firstExpense.card shouldBe stubExpense.card
                firstExpense.category shouldBe stubExpense.category
            }
        }
    }

    given("Stats expense") {
        `when`("Want to get expense statistic by categories") {
            then("Get expenses statistic") {
                val response = RestClient.request(ExpenseRoutes.stats, ExpenseStub.statsRequest)
                if (response !is ExpenseStatsResponseDto) {
                    error("response is not " + ExpenseStatsResponseDto::class)
                }

                response.total shouldBe ExpenseStub.statsResponse.total
                response.summary?.get(0) shouldNotBe null

                val firstSummary = response.summary?.get(0) ?: error("response summary at index 0 is null")
                val stubSummary = ExpenseStub.statsResponse.summary?.get(0) ?: error("stub summary at index 0 is null")

                firstSummary.amount shouldBe stubSummary.amount
                firstSummary.category shouldNotBe null
                firstSummary.category?.guid shouldBe stubSummary.category?.guid
                firstSummary.category?.name shouldBe stubSummary.category?.name
            }
        }
    }
})
package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import local.kotlin.learning.fm.acceptance.RestClient
import local.learning.api.models.*

object ExpenseRoutes {
    const val CREATE = "api/expense/create"
    const val READ = "api/expense/read"
    const val UPDATE = "api/expense/update"
    const val DELETE = "api/expense/delete"
    const val SEARCH = "api/expense/search"
    const val STATS = "api/expense/stats"
}

class ExpenseTest : BehaviorSpec({
//    beforeEach {
//        clearDb()
//    }

    given("Create new expense") {
//        `when`("Fill correctly all required fields") {
//            then("Expense saved") {
//                TODO("Not realized")
//            }
//        }
        `when`("Do not fill amount or it is invalid") {
            then("Get validation error") {
                val invalidAmounts = listOf(
                    null,
                    0.0,
                    -1.0,
                    -100.0
                )

                invalidAmounts.forEach { amount ->
                    val response = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = amount,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "amount"

                }
            }
        }
        `when`("Do not fill card guid or it is invalid") {
            then("Get validation error") {
                val invalidCardGuids = listOf(
                    null,
                    "224",
                    "wrong guid"
                )

                invalidCardGuids.forEach { cardGuid ->
                    val response = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.5,
                                card = cardGuid,
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "card"

                }
            }
        }
        `when`("Do not fill category guid or it is invalid") {
            then("Get validation error") {
                val invalidCategoryGuids = listOf(
                    null,
                    "224",
                    "wrong guid"
                )

                invalidCategoryGuids.forEach { categoryGuid ->
                    val response = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.5,
                                card = "d08e4713-657e-4351-b983-536c1bae51b5",
                                category = categoryGuid,
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "category"

                }
            }
        }
    }

    given("Read expense") {
        `when`("Do not fill guid or it is invalid") {
            then("Get validation error") {
                val invalidGuids = listOf(
                    "",
                    null,
                    "3434-3435"
                )

                invalidGuids.forEach { guid ->
                    val response = RestClient.request(
                        ExpenseRoutes.READ,
                        ExpenseReadRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            guid = guid
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseReadResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "guid"
                }
            }
        }
    }

    given("Update expense") {
        `when`("Do not fill guid or it is invalid") {
            then("Get validation error") {
                val invalidGuids = listOf(
                    null,
                    "224",
                    "wrong guid"
                )

                invalidGuids.forEach { guid ->
                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseObjectDto(
                                guid = guid,
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.5,
                                card = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "guid"

                }
            }
        }
        `when`("Do not fill amount or it is invalid") {
            then("Get validation error") {
                val invalidAmounts = listOf(
                    null,
                    0.0,
                    -1.0,
                    -100.0
                )

                invalidAmounts.forEach { amount ->
                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseObjectDto(
                                guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                createDt = "2023-01-01T14:46:04Z",
                                amount = amount,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "amount"

                }
            }
        }
        `when`("Do not fill card guid or it is invalid") {
            then("Get validation error") {
                val invalidCardGuids = listOf(
                    null,
                    "224",
                    "wrong guid"
                )

                invalidCardGuids.forEach { cardGuid ->
                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseObjectDto(
                                guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.5,
                                card = cardGuid,
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "card"

                }
            }
        }
        `when`("Do not fill category guid or it is invalid") {
            then("Get validation error") {
                val invalidCategoryGuids = listOf(
                    null,
                    "224",
                    "wrong guid"
                )

                invalidCategoryGuids.forEach { categoryGuid ->
                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            expense = ExpenseObjectDto(
                                guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.5,
                                card = "d08e4713-657e-4351-b983-536c1bae51b5",
                                category = categoryGuid,
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "category"

                }
            }
        }
    }

    given("Delete expense") {
        `when`("Do not fill guid or it is invalid") {
            then("Get validation error") {
                val invalidGuids = listOf(
                    "",
                    null,
                    "3434-3435"
                )

                invalidGuids.forEach { guid ->
                    val response = RestClient.request(
                        ExpenseRoutes.DELETE,
                        ExpenseDeleteRequestDto(
                            requestType = "expenseDelete",
                            requestId = "uniqueId",
                            guid = guid
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseDeleteResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "guid"
                }
            }
        }
    }

    given("Search expenses") {
        `when`("Fill invalid search filter: amount_from = 0") {
            then("Get validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseSearchRequestDto(
                        requestType = "expensesSearch",
                        requestId = "uniqueId",
                        amountFrom = 0.0,
                        amountTo = null
                    )
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()
                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount_from"
            }
        }
        `when`("Fill invalid search filter: amount_to = 0") {
            then("Get validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseSearchRequestDto(
                        requestType = "expensesSearch",
                        requestId = "uniqueId",
                        amountFrom = null,
                        amountTo = 0.0
                    )
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()
                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount_to"
            }
        }
        `when`("Fill invalid search filter: amount_to < amount_from") {
            then("Get validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseSearchRequestDto(
                        requestType = "expensesSearch",
                        requestId = "uniqueId",
                        amountFrom = 100.0,
                        amountTo = 50.0
                    )
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()
                response.errors?.shouldHaveSize(2)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount_from"

                response.errors?.get(1)?.code shouldBe "invalid_field_format"
                response.errors?.get(1)?.group shouldBe "validation"
                response.errors?.get(1)?.field shouldBe "amount_to"
            }
        }
        `when`("Fill invalid search filter: date_to < date_from") {
            then("Get validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseSearchRequestDto(
                        requestType = "expensesSearch",
                        requestId = "uniqueId",
                        dateFrom = "2023-01-02T14:46:04Z",
                        dateTo = "2023-01-01T14:46:04Z",
                    )
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()
                response.errors?.shouldHaveSize(2)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "date_from"

                response.errors?.get(1)?.code shouldBe "invalid_field_format"
                response.errors?.get(1)?.group shouldBe "validation"
                response.errors?.get(1)?.field shouldBe "date_to"
            }
        }
    }

    given("Statistic expenses") {
        `when`("Fill invalid statistic filter: date_to < date_from") {
            val response = RestClient.request(
                ExpenseRoutes.STATS,
                ExpenseStatsRequestDto(
                    requestType = "expensesSearch",
                    requestId = "uniqueId",
                    dateFrom = "2023-01-02T14:46:04Z",
                    dateTo = "2023-01-01T14:46:04Z",
                )
            )

            response.shouldBeInstanceOf<ExpenseStatsResponseDto>()
            response.errors?.shouldHaveSize(2)
            response.errors?.get(0)?.code shouldBe "invalid_field_format"
            response.errors?.get(0)?.group shouldBe "validation"
            response.errors?.get(0)?.field shouldBe "date_from"

            response.errors?.get(1)?.code shouldBe "invalid_field_format"
            response.errors?.get(1)?.group shouldBe "validation"
            response.errors?.get(1)?.field shouldBe "date_to"
        }
    }
})
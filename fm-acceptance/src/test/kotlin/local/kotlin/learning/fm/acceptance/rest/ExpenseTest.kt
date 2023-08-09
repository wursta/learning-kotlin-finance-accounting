package local.kotlin.learning.fm.acceptance.rest

import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import local.kotlin.learning.fm.acceptance.AppCompose
import local.kotlin.learning.fm.acceptance.ExpenseTestDataProvider
import local.kotlin.learning.fm.acceptance.RestClient
import local.learning.api.models.*
import local.learning.repo.arcadedb.ArcadeDbSchema
import mu.KotlinLogging

object ExpenseRoutes {
    const val CREATE = "api/expense/create"
    const val READ = "api/expense/read"
    const val UPDATE = "api/expense/update"
    const val DELETE = "api/expense/delete"
    const val SEARCH = "api/expense/search"
    const val STATS = "api/expense/stats"
}

private val log = KotlinLogging.logger {}

@OptIn(ExperimentalKotest::class)
class ExpenseTest : BehaviorSpec({

    beforeTest {
        val arcadeDbHost = AppCompose.C.hostArcadeDb
        val arcadeDbPort = AppCompose.C.portArcadeDb
        val arcadeDbName = AppCompose.arcadeDbName
        val arcadeDbUserName = AppCompose.arcadeDbUserName
        val arcadeDbUserPass = AppCompose.arcadeDbUserPass

        log.info { "Clear ArcadeDB database" }

        ArcadeDbSchema.clear(arcadeDbHost, arcadeDbPort, arcadeDbName, arcadeDbUserName, arcadeDbUserPass)
    }

    withData(
        listOf(
            ExpenseTestDataProvider(
                workMode = ExpenseRequestWorkModeDto.Mode.TEST
            ),
            ExpenseTestDataProvider(
                workMode = ExpenseRequestWorkModeDto.Mode.PROD
            )
        )
    ) { (workMode) ->
        given("Create new expense") {
            `when`("Fill correctly all required fields") {
                then("Expense saved") {
                    val response = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    response.result shouldBe ResponseResultDto.SUCCESS
                    response.errors?.shouldHaveSize(0)
                    response.expense?.guid.shouldNotBeNull()
                    response.expense?.amount shouldBe 100
                    response.expense?.card shouldBe "a8585ea8-e039-4799-b16c-06dc92f641f9"
                    response.expense?.category shouldBe "d08e4713-657e-4351-b983-536c1bae51b5"
                    response.expense?.lock.shouldNotBeNull()
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
                            ExpenseRoutes.CREATE,
                            ExpenseCreateRequestDto(
                                requestType = "expenseCreate",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Get info about expense with existed guid") {
                then("Expense info retrieved") {
                    // Create expense
                    val createResponse = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    val newExpenseGuid = createResponse.expense?.guid

                    val response = RestClient.request(
                        ExpenseRoutes.READ,
                        ExpenseReadRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newExpenseGuid
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseReadResponseDto>()
                    response.result shouldBe ResponseResultDto.SUCCESS
                    response.errors?.shouldHaveSize(0)
                    response.expense?.guid shouldBe newExpenseGuid
                }
            }
            `when`("Get info about other user expense") {
                then("Get access deny error") {
                    // Create expense
                    val createResponse = RestClient.request(
                        ExpenseRoutes.CREATE,
                        TEST_USER_1,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    val newExpenseGuid = createResponse.expense?.guid

                    val response = RestClient.request(
                        ExpenseRoutes.READ,
                        TEST_USER_2,
                        ExpenseReadRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newExpenseGuid
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseReadResponseDto>()
                    response.result shouldBe ResponseResultDto.ERROR
                    response.errors?.shouldHaveAtLeastSize(1)
                    response.errors?.get(0)?.code shouldBe "access_deny"
                    response.errors?.get(0)?.group shouldBe "access"

                }
            }
            `when`("Get info about expense with non existed guid") {
                then("Get validation error") {
                    val response = RestClient.request(
                        ExpenseRoutes.READ,
                        ExpenseReadRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = "d08e4713-657e-4351-b983-536c1bae51b5"
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseReadResponseDto>()

                    response.result shouldBe ResponseResultDto.ERROR
                    response.errors?.shouldHaveAtLeastSize(1)
                }
            }
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Expense guid existed, fill correctly all required fields") {
                then("Expense is updated") {
                    // Create expense
                    val createResponse = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    val newExpenseGuid = createResponse.expense?.guid
                    val newExpenseLock = createResponse.expense?.lock

                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseUpdate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseObjectDto(
                                guid = newExpenseGuid,
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 150.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                                lock = newExpenseLock
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()
                    response.result shouldBe ResponseResultDto.SUCCESS
                    response.errors?.shouldHaveSize(0)
                    response.expense?.guid shouldBe newExpenseGuid
                    response.expense?.amount shouldBe 150
                    response.expense?.lock.shouldNotBeNull()
                    response.expense?.lock shouldNotBe newExpenseLock
                }
            }
            `when`("Update other user expense") {
                then("Get access deny error") {
                    // Create expense
                    val createResponse = RestClient.request(
                        ExpenseRoutes.CREATE,
                        TEST_USER_1,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    val newExpenseGuid = createResponse.expense?.guid
                    val newExpenseLock = createResponse.expense?.lock

                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        TEST_USER_2,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseUpdate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseObjectDto(
                                guid = newExpenseGuid,
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 150.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                                lock = newExpenseLock
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()
                    response.result shouldBe ResponseResultDto.ERROR
                    response.errors?.shouldHaveAtLeastSize(1)
                    response.errors?.get(0)?.code shouldBe "access_deny"
                    response.errors?.get(0)?.group shouldBe "access"
                }
            }
            `when`("Expense guid existed, fill correctly all required fields") {
                then("Get validation error") {
                    val response = RestClient.request(
                        ExpenseRoutes.UPDATE,
                        ExpenseUpdateRequestDto(
                            requestType = "expenseUpdate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseObjectDto(
                                guid = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 150.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                                lock = "d08e4713-657e-4351-b983-536c1bae51b5"
                            )
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()
                    response.result shouldBe ResponseResultDto.ERROR
                    response.errors?.shouldHaveAtLeastSize(1)
                }
            }
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Delete expense with existed guid") {
                then("Delete expense") {
                    // Create expense
                    val createResponse = RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    val newExpenseGuid = createResponse.expense?.guid
                    val newExpenseLock = createResponse.expense?.lock

                    val response = RestClient.request(
                        ExpenseRoutes.DELETE,
                        ExpenseDeleteRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newExpenseGuid,
                            lock = newExpenseLock
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseDeleteResponseDto>()
                    response.result shouldBe ResponseResultDto.SUCCESS
                    response.errors?.shouldHaveSize(0)
                }
            }
            `when`("Delete other user expense") {
                then("Delete expense") {
                    // Create expense
                    val createResponse = RestClient.request(
                        ExpenseRoutes.CREATE,
                        TEST_USER_1,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T14:46:04Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<ExpenseCreateResponseDto>()
                    val newExpenseGuid = createResponse.expense?.guid
                    val newExpenseLock = createResponse.expense?.lock

                    val response = RestClient.request(
                        ExpenseRoutes.DELETE,
                        TEST_USER_2,
                        ExpenseDeleteRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newExpenseGuid,
                            lock = newExpenseLock
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseDeleteResponseDto>()
                    response.result shouldBe ResponseResultDto.ERROR
                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "access_deny"
                    response.errors?.get(0)?.group shouldBe "access"
                }
            }
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
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Fill valid search filter: amount_from = 10; amount_to = 20") {
                then("Get search results") {
                    // Create expense
                    RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-01T00:00:00Z",
                                amount = 10.50,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "d08e4713-657e-4351-b983-536c1bae51b5",
                            )
                        )
                    )

                    val response = RestClient.request(
                        ExpenseRoutes.SEARCH,
                        ExpenseSearchRequestDto(
                            requestType = "expensesSearch",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            amountFrom = 10.0,
                            amountTo = 20.0,
                            dateFrom = "2023-01-01T00:00:00Z",
                            dateTo = "2023-01-01T00:00:01Z"
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseSearchResponseDto>()
                    response.result shouldBe ResponseResultDto.SUCCESS
                    response.errors?.shouldHaveSize(0)
                    response.expenses?.shouldHaveAtLeastSize(1)
                }
            }
            `when`("Two users create expenses") {
                then("Get search results only with own expenses") {
                    // Create 2 expenses for each user
                    listOf(TEST_USER_1, TEST_USER_2).forEach {
                        RestClient.request(
                            ExpenseRoutes.CREATE,
                            it,
                            ExpenseCreateRequestDto(
                                requestType = "expenseCreate",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
                                expense = ExpenseCreateObjectDto(
                                    createDt = "2023-01-01T00:00:00Z",
                                    amount = 100.50,
                                    card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                    category = "d08e4713-657e-4351-b983-536c1bae51b5",
                                )
                            )
                        )
                    }

                    // Read expenses. Each user should have 1 created expense.
                    listOf(TEST_USER_1, TEST_USER_2).forEach {
                        val response = RestClient.request(
                            ExpenseRoutes.SEARCH,
                            it,
                            ExpenseSearchRequestDto(
                                requestType = "expensesSearch",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
                                amountFrom = 100.0,
                                amountTo = 101.0,
                                dateFrom = "2023-01-01T00:00:00Z",
                                dateTo = "2023-01-01T00:00:01Z"
                            )
                        )

                        response.shouldBeInstanceOf<ExpenseSearchResponseDto>()
                        response.result shouldBe ResponseResultDto.SUCCESS
                        response.errors?.shouldHaveSize(0)
                        response.expenses?.shouldHaveSize(1)
                    }
                }
            }
            `when`("Fill invalid search filter: amount_from = 0") {
                then("Get validation error") {
                    val response = RestClient.request(
                        ExpenseRoutes.SEARCH,
                        ExpenseSearchRequestDto(
                            requestType = "expensesSearch",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
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
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
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
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
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
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
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
            `when`("Fill valid statistic filter") {
                then("Get statistic") {
                    // Create expenses
                    RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-10T00:00:00Z",
                                amount = 10.50,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "1b3e4567-e99b-13d3-a476-446657420000",
                            )
                        )
                    )

                    RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-10T00:00:00Z",
                                amount = 20.50,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "1b3e4567-e99b-13d3-a476-446657420000",
                            )
                        )
                    )

                    RestClient.request(
                        ExpenseRoutes.CREATE,
                        ExpenseCreateRequestDto(
                            requestType = "expenseCreate",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            expense = ExpenseCreateObjectDto(
                                createDt = "2023-01-10T00:00:00Z",
                                amount = 100.00,
                                card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c",
                            )
                        )
                    )

                    val response = RestClient.request(
                        ExpenseRoutes.STATS,
                        ExpenseStatsRequestDto(
                            requestType = "expensesSearch",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
                            dateFrom = "2023-01-10T00:00:00Z",
                            dateTo = "2023-01-10T00:00:01Z"
                        )
                    )

                    response.shouldBeInstanceOf<ExpenseStatsResponseDto>()
                    response.result shouldBe ResponseResultDto.SUCCESS
                    response.errors?.shouldHaveSize(0)
                    response.total shouldBe 131
                    response.summary?.shouldHaveSize(2)
                    response.summary?.get(0)?.amount shouldBe 31
                    response.summary?.get(0)?.category?.guid shouldBe "1b3e4567-e99b-13d3-a476-446657420000"

                    response.summary?.get(1)?.amount shouldBe 100
                    response.summary?.get(1)?.category?.guid shouldBe "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
                }
            }
            `when`("Two users get statistic") {
                then("Get statistic only for own expenses") {
                    listOf(TEST_USER_1, TEST_USER_2).forEach {
                        // Create expenses
                        RestClient.request(
                            ExpenseRoutes.CREATE,
                            it,
                            ExpenseCreateRequestDto(
                                requestType = "expenseCreate",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
                                expense = ExpenseCreateObjectDto(
                                    createDt = "2023-02-10T00:00:00Z",
                                    amount = 10.50,
                                    card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                    category = "1b3e4567-e99b-13d3-a476-446657420000",
                                )
                            )
                        )

                        RestClient.request(
                            ExpenseRoutes.CREATE,
                            it,
                            ExpenseCreateRequestDto(
                                requestType = "expenseCreate",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
                                expense = ExpenseCreateObjectDto(
                                    createDt = "2023-02-10T00:00:00Z",
                                    amount = 20.50,
                                    card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                    category = "1b3e4567-e99b-13d3-a476-446657420000",
                                )
                            )
                        )

                        RestClient.request(
                            ExpenseRoutes.CREATE,
                            it,
                            ExpenseCreateRequestDto(
                                requestType = "expenseCreate",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
                                expense = ExpenseCreateObjectDto(
                                    createDt = "2023-02-10T00:00:00Z",
                                    amount = 100.00,
                                    card = "a8585ea8-e039-4799-b16c-06dc92f641f9",
                                    category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c",
                                )
                            )
                        )
                    }
                    listOf(TEST_USER_1, TEST_USER_2).forEach {
                        val response = RestClient.request(
                            ExpenseRoutes.STATS,
                            it,
                            ExpenseStatsRequestDto(
                                requestType = "expensesSearch",
                                requestId = "uniqueId",
                                workMode = ExpenseRequestWorkModeDto(
                                    mode = workMode
                                ),
                                dateFrom = "2023-02-10T00:00:00Z",
                                dateTo = "2023-02-10T00:00:01Z"
                            )
                        )

                        response.shouldBeInstanceOf<ExpenseStatsResponseDto>()
                        response.result shouldBe ResponseResultDto.SUCCESS
                        response.errors?.shouldHaveSize(0)
                        response.total shouldBe 131
                        response.summary?.shouldHaveSize(2)
                        response.summary?.get(0)?.amount shouldBe 31
                        response.summary?.get(0)?.category?.guid shouldBe "1b3e4567-e99b-13d3-a476-446657420000"

                        response.summary?.get(1)?.amount shouldBe 100
                        response.summary?.get(1)?.category?.guid shouldBe "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
                    }
                }
            }
            `when`("Fill invalid statistic filter: date_to < date_from") {
                then("Get validation errors") {
                    val response = RestClient.request(
                        ExpenseRoutes.STATS,
                        ExpenseStatsRequestDto(
                            requestType = "expensesSearch",
                            requestId = "uniqueId",
                            workMode = ExpenseRequestWorkModeDto(
                                mode = workMode
                            ),
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
        }
    }
})
package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import local.kotlin.learning.fm.acceptance.RestClient
import local.kotlin.learning.fm.acceptance.stub.ExpenseStub
import local.learning.api.models.*

class ExpenseStubTest: BehaviorSpec({
    given("Create new expense") {
        `when`("Get stub for successful case") {
            then("Get stub with created expense") {
                val response = RestClient.request(
                    ExpenseRoutes.CREATE,
                    ExpenseStub.getCreateRequest(ExpenseRequestWorkModeDto.StubCase.SUCCESS)
                )
                response shouldBe ExpenseStub.createResponse
            }
        }

        `when`("Get stub for invalid case: incorrect amount") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.CREATE,
                    ExpenseStub.getCreateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_AMOUNT)
                )

                response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount"
            }
        }

        `when`("Get stub for invalid case: incorrect card guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.CREATE,
                    ExpenseStub.getCreateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_CARD_GUID)
                )

                response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "card"
            }
        }

        `when`("Get stub for invalid case: incorrect category guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.CREATE,
                    ExpenseStub.getCreateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_CATEGORY_GUID)
                )

                response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "category"
            }
        }

        `when`("Request invalid stub case for create") {
            then("Get validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.CREATE,
                    ExpenseStub.getCreateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_GUID)
                )
                response.shouldBeInstanceOf<ExpenseCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_stub_case"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "stubCase"
            }
        }
    }

    given("Read expense") {
        `when`("Get stub for successful case") {
            then("Get stub with expense") {
                val response = RestClient.request(
                    ExpenseRoutes.READ,
                    ExpenseStub.getReadRequest(ExpenseRequestWorkModeDto.StubCase.SUCCESS)
                )

                response shouldBe ExpenseStub.readResponse
            }
        }

        `when`("Get stub for invalid case: incorrect guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.READ,
                    ExpenseStub.getReadRequest(ExpenseRequestWorkModeDto.StubCase.BAD_GUID)
                )

                response.shouldBeInstanceOf<ExpenseReadResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "guid"
            }
        }
    }

    given("Update expense") {
        `when`("Get stub for successful case") {
            then("Get stub with updated expense") {
                val response = RestClient.request(
                    ExpenseRoutes.UPDATE,
                    ExpenseStub.getUpdateRequest(ExpenseRequestWorkModeDto.StubCase.SUCCESS)
                )

                response shouldBe ExpenseStub.updateResponse
            }
        }

        `when`("Get stub for invalid case: incorrect guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.UPDATE,
                    ExpenseStub.getUpdateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_GUID)
                )

                response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "guid"
            }
        }

        `when`("Get stub for invalid case: incorrect amount") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.UPDATE,
                    ExpenseStub.getUpdateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_AMOUNT)
                )

                response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount"
            }
        }

        `when`("Get stub for invalid case: incorrect card guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.UPDATE,
                    ExpenseStub.getUpdateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_CARD_GUID)
                )

                response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "card"
            }
        }

        `when`("Get stub for invalid case: incorrect category guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.UPDATE,
                    ExpenseStub.getUpdateRequest(ExpenseRequestWorkModeDto.StubCase.BAD_CATEGORY_GUID)
                )

                response.shouldBeInstanceOf<ExpenseUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "category"
            }
        }
    }

    given("Delete expense") {
        `when`("Get stub for successful case") {
            then("Get stub with deleted expense") {
                val response = RestClient.request(
                    ExpenseRoutes.DELETE,
                    ExpenseStub.getDeleteRequest(ExpenseRequestWorkModeDto.StubCase.SUCCESS)
                )

                response shouldBe ExpenseStub.deleteResponse
            }
        }

        `when`("Get stub for invalid case: incorrect guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    ExpenseRoutes.DELETE,
                    ExpenseStub.getDeleteRequest(ExpenseRequestWorkModeDto.StubCase.BAD_GUID)
                )

                response.shouldBeInstanceOf<ExpenseDeleteResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "guid"
            }
        }
    }

    given("Search expense") {
        `when`("Get stub for successful case") {
            then("Get stub with filtered expenses list") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseStub.getSearchRequest(ExpenseRequestWorkModeDto.StubCase.SUCCESS)
                )

                response shouldBe ExpenseStub.searchResponse
            }
        }

        `when`("Get stub for invalid case: incorrect amount_from") {
            then("Get stub with validation errors") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseStub.getSearchRequest(ExpenseRequestWorkModeDto.StubCase.BAD_SEARCH_FILTER_AMOUNT_FROM)
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount_from"
            }
        }

        `when`("Get stub for invalid case: incorrect amount_to") {
            then("Get stub with validation errors") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseStub.getSearchRequest(ExpenseRequestWorkModeDto.StubCase.BAD_SEARCH_FILTER_AMOUNT_TO)
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "amount_to"
            }
        }

        `when`("Get stub for invalid case: incorrect sources") {
            then("Get stub with validation errors") {
                val response = RestClient.request(
                    ExpenseRoutes.SEARCH,
                    ExpenseStub.getSearchRequest(ExpenseRequestWorkModeDto.StubCase.BAD_SEARCH_FILTER_SOURCES)
                )

                response.shouldBeInstanceOf<ExpenseSearchResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "sources"
            }
        }
    }

    given("Stats expense") {
        `when`("Get stub for successful case") {
            then("Get stub with expenses statistic") {
                val response = RestClient.request(
                    ExpenseRoutes.STATS,
                    ExpenseStub.getStatisticRequest(ExpenseRequestWorkModeDto.StubCase.SUCCESS)
                )

                response shouldBe ExpenseStub.statsResponse
            }
        }

        `when`("Get stub for invalid case: incorrect date_from") {
            then("Get stub with validation errors") {
                val response = RestClient.request(
                    ExpenseRoutes.STATS,
                    ExpenseStub.getStatisticRequest(ExpenseRequestWorkModeDto.StubCase.BAD_STATISTIC_FILTER_DATE_FROM)
                )

                response.shouldBeInstanceOf<ExpenseStatsResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "date_from"
            }
        }

        `when`("Get stub for invalid case: incorrect date_to") {
            then("Get stub with validation errors") {
                val response = RestClient.request(
                    ExpenseRoutes.STATS,
                    ExpenseStub.getStatisticRequest(ExpenseRequestWorkModeDto.StubCase.BAD_STATISTIC_FILTER_DATE_TO)
                )

                response.shouldBeInstanceOf<ExpenseStatsResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "date_to"
            }
        }
    }
})
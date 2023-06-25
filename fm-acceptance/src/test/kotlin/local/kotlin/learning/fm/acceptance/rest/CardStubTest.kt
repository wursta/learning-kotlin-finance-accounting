package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import local.kotlin.learning.fm.acceptance.RestClient
import local.kotlin.learning.fm.acceptance.stub.CardStub
import local.learning.api.models.*

class CardStubTest : BehaviorSpec({
    given("Create new bank card") {
        `when`("Get stub for successful case") {
            then("Get stub with created card") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.SUCCESS)
                )
                response shouldBe CardStub.createResponse
            }
        }
        `when`("Get stub for invalid case: bad number") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.BAD_NUMBER)
                )
                response.shouldBeInstanceOf<CardCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "number"
            }
        }

        `when`("Get stub for invalid case: bad valid_for") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.BAD_VALID_FOR)
                )
                response.shouldBeInstanceOf<CardCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "valid_for"
            }
        }

        `when`("Get stub for invalid case: bad owner") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.BAD_OWNER)
                )
                response.shouldBeInstanceOf<CardCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "owner"
            }
        }

        `when`("Get stub for invalid case: bad bank guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.BAD_BANK_GUID)
                )
                response.shouldBeInstanceOf<CardCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "bank_guid"
            }
        }

        `when`("Request invalid stub case for create") {
            then("Get validation error") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.BAD_GUID)
                )
                response.shouldBeInstanceOf<CardCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_stub_case"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "stubCase"
            }
        }
    }

    given("Read card") {
        `when`("Get stub for successful case") {
            then("Get stub with card info") {
                val response = RestClient.request(
                    CardRoutes.READ,
                    CardStub.getReadRequest(CardRequestWorkModeDto.StubCase.SUCCESS)
                )
                response shouldBe CardStub.readResponse
            }
        }

        `when`("Get stub for invalid case: incorrect card guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.READ,
                    CardStub.getReadRequest(CardRequestWorkModeDto.StubCase.BAD_GUID)
                )
                response.shouldBeInstanceOf<CardReadResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "guid"
            }
        }

        `when`("Request invalid stub case for read") {
            then("Get validation error") {
                val response = RestClient.request(
                    CardRoutes.READ,
                    CardStub.getReadRequest(CardRequestWorkModeDto.StubCase.BAD_NUMBER)
                )
                response.shouldBeInstanceOf<CardReadResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_stub_case"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "stubCase"
            }
        }
    }

    given("Update card") {
        `when`("Get stub for successful case") {
            then("Get stub with card info") {
                val response = RestClient.request(
                    CardRoutes.UPDATE,
                    CardStub.getUpdateRequest(CardRequestWorkModeDto.StubCase.SUCCESS)
                )
                response shouldBe CardStub.updateResponse
            }
        }

        `when`("Get stub for invalid case: bad guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.UPDATE,
                    CardStub.getUpdateRequest(CardRequestWorkModeDto.StubCase.BAD_GUID)
                )
                response.shouldBeInstanceOf<CardUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "guid"
            }
        }

        `when`("Get stub for invalid case: bad number") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.UPDATE,
                    CardStub.getUpdateRequest(CardRequestWorkModeDto.StubCase.BAD_NUMBER)
                )
                response.shouldBeInstanceOf<CardUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "number"
            }
        }

        `when`("Get stub for invalid case: bad valid_for") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.UPDATE,
                    CardStub.getUpdateRequest(CardRequestWorkModeDto.StubCase.BAD_VALID_FOR)
                )
                response.shouldBeInstanceOf<CardUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "valid_for"
            }
        }

        `when`("Get stub for invalid case: bad owner") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.UPDATE,
                    CardStub.getUpdateRequest(CardRequestWorkModeDto.StubCase.BAD_OWNER)
                )
                response.shouldBeInstanceOf<CardUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "owner"
            }
        }

        `when`("Get stub for invalid case: bad bank guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.UPDATE,
                    CardStub.getUpdateRequest(CardRequestWorkModeDto.StubCase.BAD_BANK_GUID)
                )
                response.shouldBeInstanceOf<CardUpdateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "bank_guid"
            }
        }

        `when`("Request invalid stub case for create") {
            then("Get validation error") {
                val response = RestClient.request(
                    CardRoutes.CREATE,
                    CardStub.getCreateRequest(CardRequestWorkModeDto.StubCase.BAD_GUID)
                )
                response.shouldBeInstanceOf<CardCreateResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_stub_case"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "stubCase"
            }
        }
    }

    given("Delete card") {
        `when`("Get stub for successful case") {
            then("Get stub with card info") {
                val response = RestClient.request(
                    CardRoutes.DELETE,
                    CardStub.getDeleteRequest(CardRequestWorkModeDto.StubCase.SUCCESS)
                )
                response shouldBe CardStub.deleteResponse
            }
        }

        `when`("Get stub for invalid case: incorrect card guid") {
            then("Get stub with validation error") {
                val response = RestClient.request(
                    CardRoutes.DELETE,
                    CardStub.getDeleteRequest(CardRequestWorkModeDto.StubCase.BAD_GUID)
                )
                response.shouldBeInstanceOf<CardDeleteResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_field_format"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "guid"
            }
        }

        `when`("Request invalid stub case for read") {
            then("Get validation error") {
                val response = RestClient.request(
                    CardRoutes.DELETE,
                    CardStub.getDeleteRequest(CardRequestWorkModeDto.StubCase.BAD_NUMBER)
                )
                response.shouldBeInstanceOf<CardDeleteResponseDto>()

                response.errors?.shouldHaveSize(1)
                response.errors?.get(0)?.code shouldBe "invalid_stub_case"
                response.errors?.get(0)?.group shouldBe "validation"
                response.errors?.get(0)?.field shouldBe "stubCase"
            }
        }
    }
})
package local.kotlin.learning.fm.acceptance.rest

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import local.kotlin.learning.fm.acceptance.RestClient
import local.learning.api.models.*

object CardRoutes {
    const val CREATE = "api/card/create"
    const val READ = "api/card/read"
    const val UPDATE = "api/card/update"
    const val DELETE = "api/card/delete"
}

class CardTest : BehaviorSpec({

//    beforeEach {
//        clearDb()
//    }

    given("Create new card") {
//        `when`("Fill correctly all required fields") {
//            then("Card is saved") {
//                TODO("Not realized")
//            }
//        }
        `when`("Do not fill card number or number is invalid") {
            then("Get validation error") {
                val invalidNumbers = listOf(
                    "wrong number",
                    "",
                    null,
                    "223423245"
                )

                invalidNumbers.forEach { number ->
                    val response = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            card = CardCreateObjectDto(
                                number = number,
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardCreateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "number"
                }
            }
        }

        `when`("Do not fill card valid_for or it is invalid") {
            then("Get validation error") {
                val invalidValidFor = listOf(
                    "wrong valid for",
                    "",
                    null,
                    "2022--02"
                )

                invalidValidFor.forEach { validFor ->
                    val response = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            card = CardCreateObjectDto(
                                number = "6915542779916368",
                                validFor = validFor,
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardCreateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "valid_for"
                }
            }
        }
        `when`("Do not fill card owner name or it is invalid") {
            then("Get validation error") {
                val invalidOwners = listOf(
                    "",
                    null,
                    "O",
                    "TOOOOOOOOOOOO LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG"
                )

                invalidOwners.forEach { owner ->
                    val response = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            card = CardCreateObjectDto(
                                number = "6915542779916368",
                                validFor = "2025-01",
                                owner = owner,
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardCreateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "owner"
                }
            }
        }
    }

    given("Read card") {
//        `when`("Get info about card with existed guid") {
//            then("Card info retrieved") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Get info about non existed card") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
        `when`("Do not fill card guid or it is invalid") {
            then("Get validation error") {
                val invalidGuids = listOf(
                    "",
                    null,
                    "3434-3435"
                )

                invalidGuids.forEach { guid ->
                    val response = RestClient.request(
                        CardRoutes.READ,
                        CardReadRequestDto(
                            requestType = "cardRead",
                            requestId = "uniqueId",
                            guid = guid
                        )
                    )

                    response.shouldBeInstanceOf<CardReadResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "guid"
                }
            }
        }
    }

    given("Update card") {
//        `when`("Card guid existed, fill correctly all required fields") {
//            then("Card is updated") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Try to update non existed card") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
        `when`("Do not fill card guid or it is invalid") {
            then("Get validation error") {
                val invalidGuids = listOf(
                    "",
                    null,
                    "3434-3435"
                )

                invalidGuids.forEach { guid ->
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        CardUpdateRequestDto(
                            requestType = "cardRead",
                            requestId = "uniqueId",
                            card = CardObjectDto(
                                guid = guid,
                                number = "6915542779916368",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = BankObjectDto(
                                    guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                    name = "Sberbank"
                                )
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "guid"
                }
            }
        }
        `when`("Do not fill card number or number is invalid") {
            then("Get validation error") {
                val invalidNumbers = listOf(
                    "wrong number",
                    "",
                    null,
                    "223423245"
                )

                invalidNumbers.forEach { number ->
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        CardUpdateRequestDto(
                            requestType = "cardUpdate",
                            requestId = "uniqueId",
                            card = CardObjectDto(
                                guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                number = number,
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = BankObjectDto(
                                    guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                    name = "Sberbank"
                                )
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "number"
                }
            }
        }
        `when`("Do not fill card valid_for date or it is invalid") {
            then("Get validation error") {
                val invalidValidFor = listOf(
                    "wrong valid for",
                    "",
                    null,
                    "2022--02"
                )

                invalidValidFor.forEach { validFor ->
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        CardUpdateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            card = CardObjectDto(
                                guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                number = "6915542779916368",
                                validFor = validFor,
                                owner = "SAZONOV MIKHAIL",
                                bank = BankObjectDto(
                                    guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                    name = "Sberbank"
                                )
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "valid_for"
                }
            }
        }
        `when`("Do not fill card owner name ot it is invalid") {
            then("Get validation error") {
                val invalidOwners = listOf(
                    "",
                    null,
                    "O",
                    "TOOOOOOOOOOOO LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG"
                )

                invalidOwners.forEach { owner ->
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        CardUpdateRequestDto(
                            requestType = "cardUpdate",
                            requestId = "uniqueId",
                            card = CardObjectDto(
                                guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                number = "6915542779916368",
                                validFor = "2025-01",
                                owner = owner,
                                bank = BankObjectDto(
                                    guid = "eb770343-0339-43a9-aee9-54dccba2cf8e",
                                    name = "Sberbank"
                                )
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "owner"
                }
            }
        }
    }

    given("Delete card") {
//        `when`("Delete card") {
//            then("Card is deleted") {
//                TODO("Not realized")
//            }
//        }
//        `when`("Try to delete non existed card") {
//            then("Get validation error") {
//                TODO("Not realized")
//            }
//        }
        `when`("Do not fill card guid or it is invalid") {
            then("Get validation error") {
                val invalidGuids = listOf(
                    "",
                    null,
                    "3434-3435"
                )

                invalidGuids.forEach { guid ->
                    val response = RestClient.request(
                        CardRoutes.DELETE,
                        CardDeleteRequestDto(
                            requestType = "cardDelete",
                            requestId = "uniqueId",
                            guid = guid
                        )
                    )

                    response.shouldBeInstanceOf<CardDeleteResponseDto>()

                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "invalid_field_format"
                    response.errors?.get(0)?.group shouldBe "validation"
                    response.errors?.get(0)?.field shouldBe "guid"
                }
            }
        }
    }
})
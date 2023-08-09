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
import local.kotlin.learning.fm.acceptance.CardTestDataProvider
import local.kotlin.learning.fm.acceptance.RestClient
import local.learning.api.models.*
import local.learning.repo.arcadedb.ArcadeDbSchema
import mu.KotlinLogging

object CardRoutes {
    const val CREATE = "api/card/create"
    const val READ = "api/card/read"
    const val UPDATE = "api/card/update"
    const val DELETE = "api/card/delete"
}

private val log = KotlinLogging.logger {}

@OptIn(ExperimentalKotest::class)
class CardTest : BehaviorSpec({

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
            CardTestDataProvider(
                workMode = CardRequestWorkModeDto.Mode.TEST
            ),
            CardTestDataProvider(
                workMode = CardRequestWorkModeDto.Mode.PROD
            )
        )
    ) { (workMode) ->
        given("Create new card") {
            `when`("Fill correctly all required fields") {
                then("Card is saved") {
                    val response = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardCreateResponseDto>()
                    response.result.shouldBe(ResponseResultDto.SUCCESS)
                    response.errors?.shouldHaveSize(0)
                    response.card.shouldBeInstanceOf<CardObjectDto>()
                    response.card?.guid.shouldNotBeNull()
                    response.card?.number shouldBe "5191891428863955"
                    response.card?.validFor shouldBe "2026-01"
                    response.card?.owner shouldBe "SAZONOV MIKHAIL"
                    response.card?.bank?.guid shouldBe "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                    response.card?.lock.shouldNotBeNull()
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
                            CardRoutes.CREATE,
                            CardCreateRequestDto(
                                requestType = "cardCreate",
                                requestId = "uniqueId",
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Get info about card with existed guid") {
                then("Card info retrieved") {
                    // Create card
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid
                    val newCardLockGuid = createResponse.card?.lock

                    // Read card
                    val response = RestClient.request(
                        CardRoutes.READ,
                        CardReadRequestDto(
                            requestType = "cardRead",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newCardGuid
                        )
                    )

                    response.shouldBeInstanceOf<CardReadResponseDto>()
                    response.result.shouldBe(ResponseResultDto.SUCCESS)
                    response.errors?.shouldHaveSize(0)
                    response.card.shouldBeInstanceOf<CardObjectDto>()
                    response.card?.guid shouldBe newCardGuid
                    response.card?.number shouldBe "5191891428863955"
                    response.card?.validFor shouldBe "2026-01"
                    response.card?.owner shouldBe "SAZONOV MIKHAIL"
                    response.card?.bank?.guid shouldBe "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                    response.card?.lock shouldBe newCardLockGuid
                }
            }
            `when`("Get info about non existed card") {
                then("Get validation error") {
                    val response = RestClient.request(
                        CardRoutes.READ,
                        CardReadRequestDto(
                            requestType = "cardRead",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                        )
                    )

                    response.shouldBeInstanceOf<CardReadResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveAtLeastSize(1)
                }
            }
            `when`("Get info about other user card") {
                then("Get access deny error") {
                    // Create card from test user 1
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        TEST_USER_1,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid

                    // Read card from test user 2
                    val response = RestClient.request(
                        CardRoutes.READ,
                        TEST_USER_2,
                        CardReadRequestDto(
                            requestType = "cardRead",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newCardGuid
                        )
                    )

                    response.shouldBeInstanceOf<CardReadResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveSize(1)
                    response.errors?.get(0)?.code shouldBe "access_deny"
                    response.errors?.get(0)?.group shouldBe "access"
                }
            }
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Card guid existed, fill correctly all required fields") {
                then("Card is updated") {
                    // Create card
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid
                    val newCardLockGuid = createResponse.card?.lock

                    // Update card
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        CardUpdateRequestDto(
                            requestType = "cardUpdate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardObjectDto(
                                guid = newCardGuid,
                                number = "3333444455556666",
                                validFor = "2023-01",
                                owner = "IVANOV PETR",
                                bank = BankObjectDto(
                                    guid = "428488f0-b878-4080-a2fa-6fbdb5d7790a"
                                ),
                                lock = newCardLockGuid
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()
                    response.result.shouldBe(ResponseResultDto.SUCCESS)
                    response.errors?.shouldHaveSize(0)
                    response.card.shouldBeInstanceOf<CardObjectDto>()
                    response.card?.guid shouldBe newCardGuid
                    response.card?.number shouldBe "3333444455556666"
                    response.card?.validFor shouldBe "2023-01"
                    response.card?.owner shouldBe "IVANOV PETR"
                    response.card?.bank?.guid shouldBe "428488f0-b878-4080-a2fa-6fbdb5d7790a"
                    response.card?.lock shouldNotBe newCardLockGuid
                    response.card?.lock.shouldNotBeNull()
                }
            }
            `when`("Try to update non existed card") {
                then("Get validation error") {
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        CardUpdateRequestDto(
                            requestType = "cardUpdate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardObjectDto(
                                guid = "428488f0-b878-4080-a2fa-6fbdb5d7790a",
                                number = "3333444455556666",
                                validFor = "2023-01",
                                owner = "IVANOV PETR",
                                bank = BankObjectDto(
                                    guid = "428488f0-b878-4080-a2fa-6fbdb5d7790a"
                                ),
                                lock = "428488f0-b878-4080-a2fa-6fbdb5d7790a"
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveAtLeastSize(1)
                }
            }
            `when`("Try to update other user card") {
                then("Get access deny error") {
                    // Create card
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        TEST_USER_1,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid
                    val newCardLockGuid = createResponse.card?.lock

                    // Update card
                    val response = RestClient.request(
                        CardRoutes.UPDATE,
                        TEST_USER_2,
                        CardUpdateRequestDto(
                            requestType = "cardUpdate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardObjectDto(
                                guid = newCardGuid,
                                number = "3333444455556666",
                                validFor = "2023-01",
                                owner = "IVANOV PETR",
                                bank = BankObjectDto(
                                    guid = "428488f0-b878-4080-a2fa-6fbdb5d7790a"
                                ),
                                lock = newCardLockGuid
                            )
                        )
                    )

                    response.shouldBeInstanceOf<CardUpdateResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveAtLeastSize(1)
                    response.errors?.get(0)?.code shouldBe "access_deny"
                    response.errors?.get(0)?.group shouldBe "access"

                }
            }
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
            `when`("Delete card") {
                then("Card is deleted") {
                    // Create card
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid
                    val newCardLockGuid = createResponse.card?.lock

                    val response = RestClient.request(
                        CardRoutes.DELETE,
                        CardDeleteRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newCardGuid,
                            lock = newCardLockGuid
                        )
                    )

                    response.shouldBeInstanceOf<CardDeleteResponseDto>()
                    response.result.shouldBe(ResponseResultDto.SUCCESS)
                    response.errors?.shouldHaveSize(0)
                }
            }
            `when`("Try to delete other user card") {
                then("Get access deny error") {
                    // Create card
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        TEST_USER_1,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid
                    val newCardLockGuid = createResponse.card?.lock

                    val response = RestClient.request(
                        CardRoutes.DELETE,
                        TEST_USER_2,
                        CardDeleteRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newCardGuid,
                            lock = newCardLockGuid
                        )
                    )

                    response.shouldBeInstanceOf<CardDeleteResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveAtLeastSize(1)
                    response.errors?.get(0)?.code shouldBe "access_deny"
                    response.errors?.get(0)?.group shouldBe "access"
                }
            }
            `when`("Try to delete card with invalid lock") {
                then("Get validation error") {
                    // Create card
                    val createResponse = RestClient.request(
                        CardRoutes.CREATE,
                        CardCreateRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            card = CardCreateObjectDto(
                                number = "5191891428863955",
                                validFor = "2026-01",
                                owner = "SAZONOV MIKHAIL",
                                bank = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                            )
                        )
                    )

                    createResponse.shouldBeInstanceOf<CardCreateResponseDto>()

                    val newCardGuid = createResponse.card?.guid

                    val response = RestClient.request(
                        CardRoutes.DELETE,
                        CardDeleteRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = newCardGuid,
                            lock = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                        )
                    )

                    response.shouldBeInstanceOf<CardDeleteResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveAtLeastSize(1)
                }
            }
            `when`("Try to delete non existed card") {
                then("Get validation error") {
                    val response = RestClient.request(
                        CardRoutes.DELETE,
                        CardDeleteRequestDto(
                            requestType = "cardCreate",
                            requestId = "uniqueId",
                            workMode = CardRequestWorkModeDto(
                                mode = workMode
                            ),
                            guid = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2",
                            lock = "e69486d1-dae7-4ade-aa82-b4e98f65f0f2"
                        )
                    )

                    response.shouldBeInstanceOf<CardDeleteResponseDto>()
                    response.result.shouldBe(ResponseResultDto.ERROR)
                    response.errors?.shouldHaveAtLeastSize(1)
                }
            }
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
                                workMode = CardRequestWorkModeDto(
                                    mode = workMode
                                ),
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
    }
})
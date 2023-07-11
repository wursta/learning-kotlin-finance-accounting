package local.learning.app.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import local.learning.common.CardContext
import local.learning.common.CorSettings
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.State
import local.learning.common.models.WorkMode
import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardGuid
import local.learning.repo.inmemory.CardInMemoryRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CardValidationTest {
    private val processor = CardProcessor(
        CorSettings(
            cardRepoTest = CardInMemoryRepository()
        )
    )

    @Test
    fun createValid() = runTest {
        val ctx = CardContext(
            command = CardCommand.CREATE,
            state = State.NONE,
            workMode = WorkMode.TEST,
            cardRequest = Card(
                number = "0000000000000000",
                owner = "   SAZONOV MIKHAIL   ",
                validFor = "2023-01",
                bankGuid = BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
            )
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size == 0 }
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.cardRequest.number, ctx.cardValidating.number)
        assertEquals("SAZONOV MIKHAIL", ctx.cardValidating.owner)
        assertEquals(ctx.cardRequest.validFor, ctx.cardValidating.validFor)
        assertEquals(ctx.cardRequest.bankGuid, ctx.cardValidating.bankGuid)
    }

    @Test
    fun createInvalid() = runTest {
        val invalidCards = listOf(
            Card(
                number = "wrong number",
                owner = "s..",
                validFor = "2023-01-01",
                bankGuid = BankGuid("wrong guid")
            ),
            Card(
                number = "wrong number",
                owner = "toooooooooooooooooooooooooooooooooooooooooooo loooooooooooooooooooooooooooong",
                validFor = "2023",
                bankGuid = BankGuid("wrong guid")
            )
        )

        invalidCards.forEach {
            val ctx = CardContext(
                command = CardCommand.CREATE,
                state = State.NONE,
                workMode = WorkMode.TEST,
                cardRequest = it
            )

            processor.exec(ctx)

            assertEquals(4, ctx.errors.size)

            assertEquals(State.FAILING, ctx.state)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
            assertEquals("number", ctx.errors[0].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[1].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[1].group)
            assertEquals("valid_for", ctx.errors[1].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[2].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[2].group)
            assertEquals("owner", ctx.errors[2].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[3].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[3].group)
            assertEquals("bankGuid", ctx.errors[3].field)
        }
    }

    @Test
    fun readValid() = runTest {
        val ctx = CardContext(
            command = CardCommand.READ,
            state = State.NONE,
            workMode = WorkMode.TEST,
            cardRequest = Card(
                guid = CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
            )
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size == 0 }
        assertNotEquals(State.FAILING, ctx.state)
    }

    @Test
    fun readInvalid() = runTest {
        val ctx = CardContext(
            command = CardCommand.READ,
            state = State.NONE,
            workMode = WorkMode.TEST,
            cardRequest = Card(
                guid = CardGuid("wrong number"),
            )
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }

    @Test
    fun updateValid() = runTest {
        val ctx = CardContext(
            command = CardCommand.UPDATE,
            state = State.NONE,
            workMode = WorkMode.TEST,
            cardRequest = Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "0000000000000000",
                owner = "   SAZONOV MIKHAIL   ",
                validFor = "2023-01",
                bankGuid = BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
            )
        )

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
        assertEquals(ctx.cardRequest.guid, ctx.cardValidating.guid)
        assertEquals(ctx.cardRequest.number, ctx.cardValidating.number)
        assertEquals("SAZONOV MIKHAIL", ctx.cardValidating.owner)
        assertEquals(ctx.cardRequest.validFor, ctx.cardValidating.validFor)
        assertEquals(ctx.cardRequest.bankGuid, ctx.cardValidating.bankGuid)
    }

    @Test
    fun updateInvalid() = runTest {
        val invalidCards = listOf(
            Card(
                guid = CardGuid("wrong guid"),
                number = "wrong number",
                owner = "s..",
                validFor = "2023-01-01",
                bankGuid = BankGuid("wrong guid")
            ),
            Card(
                guid = CardGuid("wrong guid"),
                number = "wrong number",
                owner = "toooooooooooooooooooooooooooooooooooooooooooo loooooooooooooooooooooooooooong",
                validFor = "2023",
                bankGuid = BankGuid("wrong guid")
            )
        )

        invalidCards.forEach {
            val ctx = CardContext(
                command = CardCommand.UPDATE,
                state = State.NONE,
                workMode = WorkMode.TEST,
                cardRequest = it
            )

            processor.exec(ctx)

            assertEquals(5, ctx.errors.size)

            assertEquals(State.FAILING, ctx.state)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
            assertEquals("guid", ctx.errors[0].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[1].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[1].group)
            assertEquals("number", ctx.errors[1].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[2].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[2].group)
            assertEquals("valid_for", ctx.errors[2].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[3].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[3].group)
            assertEquals("owner", ctx.errors[3].field)

            assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[4].code)
            assertEquals(ErrorGroup.VALIDATION, ctx.errors[4].group)
            assertEquals("bankGuid", ctx.errors[4].field)
        }
    }

    @Test
    fun deleteValid() = runTest {
        val ctx = CardContext(
            command = CardCommand.DELETE,
            state = State.NONE,
            workMode = WorkMode.TEST,
            cardRequest = Card(
                guid = CardGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6")
            )
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size == 0 }
        assertNotEquals(State.FAILING, ctx.state)
    }

    @Test
    fun deleteInvalid() = runTest {
        val ctx = CardContext(
            command = CardCommand.DELETE,
            state = State.NONE,
            workMode = WorkMode.TEST,
            cardRequest = Card(
                guid = CardGuid("wrong number"),
            )
        )

        processor.exec(ctx)

        assertEquals(1, ctx.errors.size)

        assertEquals(State.FAILING, ctx.state)

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }
}
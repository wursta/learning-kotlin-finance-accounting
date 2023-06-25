package local.learning.app.biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import local.learning.common.CardContext
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.State
import local.learning.common.models.WorkMode
import local.learning.common.models.card.CardCommand
import local.learning.common.models.card.CardStubCase
import local.learning.stubs.CardStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CardStubsTest {
    private val processor = CardProcessor()
    private val cardStub = CardStub.get()

    @Test
    fun noStubCase() = runTest {
        val ctx = CardContext(
            command = CardCommand.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.NONE
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_STUB_CASE, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
    }

    @Test()
    fun createSuccess() = runTest {
        val ctx = CardContext(
            command = CardCommand.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(cardStub.guid, ctx.cardResponse.guid)
        assertEquals(cardStub.owner, ctx.cardResponse.owner)
        assertEquals(cardStub.validFor, ctx.cardResponse.validFor)
        assertEquals(cardStub.bankGuid, ctx.cardResponse.bankGuid)
    }

    @Test
    fun createValidationErrorBadNumber() = runTest {
        val ctx = CardContext(
            command = CardCommand.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_NUMBER
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("number", ctx.errors[0].field)
    }

    @Test
    fun createValidationErrorBadValidFor() = runTest {
        val ctx = CardContext(
            command = CardCommand.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_VALID_FOR
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("valid_for", ctx.errors[0].field)
    }

    @Test
    fun createValidationErrorBadOwner() = runTest {
        val ctx = CardContext(
            command = CardCommand.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_OWNER
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("owner", ctx.errors[0].field)
    }

    @Test()
    fun readSuccess() = runTest {
        val ctx = CardContext(
            command = CardCommand.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(cardStub.guid, ctx.cardResponse.guid)
        assertEquals(cardStub.owner, ctx.cardResponse.owner)
        assertEquals(cardStub.validFor, ctx.cardResponse.validFor)
        assertEquals(cardStub.bankGuid, ctx.cardResponse.bankGuid)
    }

    @Test
    fun readValidationErrorBadGuid() = runTest {
        val ctx = CardContext(
            command = CardCommand.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_GUID
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }

    @Test()
    fun updateSuccess() = runTest {
        val ctx = CardContext(
            command = CardCommand.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(cardStub.guid, ctx.cardResponse.guid)
        assertEquals(cardStub.owner, ctx.cardResponse.owner)
        assertEquals(cardStub.validFor, ctx.cardResponse.validFor)
        assertEquals(cardStub.bankGuid, ctx.cardResponse.bankGuid)
    }

    @Test
    fun updateValidationErrorBadGuid() = runTest {
        val ctx = CardContext(
            command = CardCommand.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_GUID
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }

    @Test
    fun updateValidationErrorBadNumber() = runTest {
        val ctx = CardContext(
            command = CardCommand.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_NUMBER
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("number", ctx.errors[0].field)
    }

    @Test
    fun updateValidationErrorBadValidFor() = runTest {
        val ctx = CardContext(
            command = CardCommand.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_VALID_FOR
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("valid_for", ctx.errors[0].field)
    }

    @Test
    fun updateValidationErrorBadOwner() = runTest {
        val ctx = CardContext(
            command = CardCommand.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_OWNER
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("owner", ctx.errors[0].field)
    }

    @Test()
    fun deleteSuccess() = runTest {
        val ctx = CardContext(
            command = CardCommand.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.SUCCESS
        )

        processor.exec(ctx)

        assertEquals(cardStub.guid, ctx.cardResponse.guid)
        assertEquals(cardStub.owner, ctx.cardResponse.owner)
        assertEquals(cardStub.validFor, ctx.cardResponse.validFor)
        assertEquals(cardStub.bankGuid, ctx.cardResponse.bankGuid)
    }

    @Test
    fun deleteValidationErrorBadGuid() = runTest {
        val ctx = CardContext(
            command = CardCommand.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = CardStubCase.VALIDATION_ERROR_BAD_GUID
        )

        processor.exec(ctx)

        assertTrue { ctx.errors.size > 0 }

        assertEquals(ErrorCode.INVALID_FIELD_FORMAT, ctx.errors[0].code)
        assertEquals(ErrorGroup.VALIDATION, ctx.errors[0].group)
        assertEquals("guid", ctx.errors[0].field)
    }
}
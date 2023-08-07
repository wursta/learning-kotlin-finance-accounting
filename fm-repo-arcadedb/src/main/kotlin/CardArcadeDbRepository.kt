import com.arcadedb.query.sql.executor.EmptyResult
import com.arcadedb.remote.RemoteDatabase
import com.benasher44.uuid.uuid4
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.Error
import local.learning.common.models.LockGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardGuid
import local.learning.common.repo.ICardRepository
import local.learning.common.repo.card.DbCardGuidRequest
import local.learning.common.repo.card.DbCardRequest
import local.learning.common.repo.card.DbCardResponse

class CardArcadeDbRepository(
    private val host: String,
    private val port: Int,
    private val dbName: String,
    private val userName: String,
    private val userPassword: String,
    val randomUuid: () -> String = { uuid4().toString() }
) : ICardRepository {
    private val db by lazy {
        RemoteDatabase(host, port, dbName, userName, userPassword)
    }

    fun initRepo(vararg cards: Card) {
        cards.forEach { card ->
            db.command(
                "sql",
                "CREATE VERTEX card SET " +
                        "guid = :guid, " +
                        "number = :number, " +
                        "validFor = :validFor, " +
                        "owner = :owner, " +
                        "bankGuid = :bankGuid, " +
                        "lockGuid = :lockGuid",
                hashMapOf(
                    Pair("guid", card.guid.asString()),
                    Pair("number", card.number),
                    Pair("validFor", card.validFor),
                    Pair("owner", card.owner),
                    Pair("bankGuid", card.bankGuid.asString()),
                    Pair("lockGuid", card.lockGuid.asString())
                )
            )
        }
    }

    override suspend fun create(cardRequest: DbCardRequest): DbCardResponse {
        val guid = randomUuid()
        val card = cardRequest.card.copy(guid = CardGuid(guid), lockGuid = LockGuid(randomUuid()))

        try {
            val resultSet = db.command(
                "sql",
                "CREATE VERTEX card SET " +
                        "guid = :guid, " +
                        "number = :number, " +
                        "validFor = :validFor, " +
                        "owner = :owner, " +
                        "bankGuid = :bankGuid, " +
                        "lockGuid = :lockGuid",
                hashMapOf(
                    Pair("guid", card.guid.asString()),
                    Pair("number", card.number),
                    Pair("validFor", card.validFor),
                    Pair("owner", card.owner),
                    Pair("bankGuid", card.bankGuid.asString()),
                    Pair("lockGuid", card.lockGuid.asString())
                )
            )
            val record = resultSet.nextIfAvailable()
            return DbCardResponse(record.toCardInternal(), true)
        } catch (e: Throwable) {
            return DbCardResponse(
                null,
                false,
                listOf(
                    Error(
                        code = ErrorCode.UNKNOWN,
                        group = ErrorGroup.EXCEPTIONS,
                        message = e.message.toString(),
                        exception = e
                    )
                )
            )
        }
    }

    override suspend fun read(cardGuidRequest: DbCardGuidRequest): DbCardResponse {
        val key = cardGuidRequest.guid.takeIf { it != CardGuid.NONE }?.asString() ?: return resultErrorEmptyGuid

        val resultSet = db.command(
            "sql",
            "SELECT FROM card WHERE guid = :guid",
            hashMapOf(
                Pair("guid", key)
            )
        )

        val record = resultSet.nextIfAvailable()

        if (record is EmptyResult) {
            return resultErrorNotFound
        }

        return DbCardResponse(record.toCardInternal(), true)
    }

    override suspend fun update(cardRequest: DbCardRequest): DbCardResponse {
        val key = cardRequest.card.guid.takeIf { it != CardGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest =
            cardRequest.card.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        val resultSetForUpdate = db.command(
            "sql",
            "SELECT FROM card WHERE guid = :guid",
            hashMapOf(
                Pair("guid", key),
                Pair("lockGuid", lockFromRequest)
            )
        )

        val recordForUpdate = resultSetForUpdate.nextIfAvailable()

        if (recordForUpdate is EmptyResult) {
            return resultErrorNotFound
        }

        if (recordForUpdate.getProperty<String>("lockGuid") != lockFromRequest) {
            return resultErrorInvalidLock
        }

        val resultSet = db.command(
            "sql",
            "UPDATE card SET " +
                    "number = :number, " +
                    "validFor = :validFor, " +
                    "owner = :owner, " +
                    "bankGuid = :bankGuid, " +
                    "lockGuid = :newLockGuid " +
                    "RETURN AFTER " +
                    "WHERE guid = :guid AND lockGuid = :oldLockGuid",
            hashMapOf(
                Pair("guid", key),
                Pair("number", cardRequest.card.number),
                Pair("validFor", cardRequest.card.validFor),
                Pair("owner", cardRequest.card.owner),
                Pair("bankGuid", cardRequest.card.bankGuid.asString()),
                Pair("newLockGuid", randomUuid()),
                Pair("oldLockGuid", lockFromRequest)
            )
        )

        val record = resultSet.nextIfAvailable()

        if (record is EmptyResult) {
            return resultErrorInvalidLock
        }

        return DbCardResponse(record.toCardInternal(), true)
    }

    override suspend fun delete(cardRequest: DbCardGuidRequest): DbCardResponse {
        val key = cardRequest.guid.takeIf { it != CardGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest =
            cardRequest.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        val resultSetForDelete = db.command(
            "sql",
            "SELECT FROM card WHERE guid = :guid",
            hashMapOf(
                Pair("guid", key),
                Pair("lockGuid", lockFromRequest)
            )
        )

        val recordForDelete = resultSetForDelete.nextIfAvailable()

        if (recordForDelete is EmptyResult) {
            return resultErrorNotFound
        }

        if (recordForDelete.getProperty<String>("lockGuid") != lockFromRequest) {
            return resultErrorInvalidLock
        }

        val resultSet = db.command(
            "sql",
            "DELETE FROM card " +
                    "RETURN BEFORE " +
                    "WHERE guid = :guid AND lockGuid = :lockGuid",
            hashMapOf(
                Pair("guid", key),
                Pair("lockGuid", lockFromRequest)
            )
        )

        val record = resultSet.nextIfAvailable()

        if (record is EmptyResult) {
            return resultErrorInvalidLock
        }

        return DbCardResponse(record.toCardInternal(), true)
    }

    companion object {
        val resultErrorEmptyGuid = DbCardResponse(
            card = null,
            success = false,
            errors = listOf(
                Error(
                    code = ErrorCode.EMPTY_GUID,
                    group = ErrorGroup.VALIDATION,
                    field = "guid",
                    message = "Card guid must not be null or blank"
                )
            )
        )

        val resultErrorEmptyLock = DbCardResponse(
            card = null,
            success = false,
            errors = listOf(
                Error(
                    code = ErrorCode.EMPTY_LOCK,
                    group = ErrorGroup.VALIDATION,
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )

        val resultErrorInvalidLock = DbCardResponse(
            card = null,
            success = false,
            errors = listOf(
                Error(
                    code = ErrorCode.INVALID_LOCK,
                    group = ErrorGroup.VALIDATION,
                    field = "lock",
                    message = "The object has been changed concurrently by another user or process"
                )
            )
        )

        val resultErrorNotFound = DbCardResponse(
            card = null,
            success = false,
            errors = listOf(
                Error(
                    code = ErrorCode.NOT_FOUND,
                    group = ErrorGroup.VALIDATION,
                    field = "guid",
                    message = "Card not found"
                )
            )
        )
    }
}
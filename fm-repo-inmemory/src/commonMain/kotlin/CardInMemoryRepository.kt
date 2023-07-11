package local.learning.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
import local.learning.repo.inmemory.models.CardEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class CardInMemoryRepository(
    initObjects: List<Card> = emptyList(),
    ttl: Duration = 10.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : ICardRepository {

    private val mutex = Mutex()
    private val cache = Cache.Builder<String, CardEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            cache.put(it.guid.asString(), CardEntity(it))
        }
    }

    override suspend fun create(cardRequest: DbCardRequest): DbCardResponse {
        val key = randomUuid()
        val card = cardRequest.card.copy(guid = CardGuid(key), lockGuid = LockGuid(randomUuid()))
        val cardEntity = CardEntity(card)
        cache.put(key, cardEntity)
        return DbCardResponse(card, true)
    }

    override suspend fun read(cardGuidRequest: DbCardGuidRequest): DbCardResponse {
        val key = cardGuidRequest.guid.takeIf { it != CardGuid.NONE }?.asString() ?: return resultErrorEmptyGuid

        val cardEntity = cache.get(key) ?: return resultErrorNotFound

        return DbCardResponse(cardEntity.toInternal(), true)
    }
    override suspend fun update(cardRequest: DbCardRequest): DbCardResponse {
        val key = cardRequest.card.guid.takeIf { it != CardGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest = cardRequest.card.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldCardEntity = cache.get(key) ?: return resultErrorNotFound
            if (oldCardEntity.lockGuid != lockFromRequest) {
                return resultErrorInvalidLock
            }
            val newCardEntity = CardEntity(cardRequest.card)
            newCardEntity.lockGuid = randomUuid()
            cache.put(key, newCardEntity)
            return DbCardResponse(newCardEntity.toInternal(), true)
        }
    }

    override suspend fun delete(cardRequest: DbCardGuidRequest): DbCardResponse {
        val key = cardRequest.guid.takeIf { it != CardGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest = cardRequest.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldCardEntity = cache.get(key) ?: return resultErrorNotFound
            if (oldCardEntity.lockGuid != lockFromRequest) {
                return resultErrorInvalidLock
            }
            cache.invalidate(key)
            return DbCardResponse(oldCardEntity.toInternal(), true)
        }
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
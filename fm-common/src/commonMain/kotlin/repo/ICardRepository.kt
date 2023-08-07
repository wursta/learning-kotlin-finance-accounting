package local.learning.common.repo

import local.learning.common.repo.card.DbCardGuidRequest
import local.learning.common.repo.card.DbCardRequest
import local.learning.common.repo.card.DbCardResponse

interface ICardRepository {
    suspend fun create(cardRequest: DbCardRequest): DbCardResponse
    suspend fun read(cardGuidRequest: DbCardGuidRequest): DbCardResponse
    suspend fun update(cardRequest: DbCardRequest): DbCardResponse
    suspend fun delete(cardRequest: DbCardGuidRequest): DbCardResponse

    companion object {
        val NONE = object: ICardRepository {
            override suspend fun create(cardRequest: DbCardRequest): DbCardResponse {
                TODO("Not yet implemented")
            }

            override suspend fun read(cardGuidRequest: DbCardGuidRequest): DbCardResponse {
                TODO("Not yet implemented")
            }

            override suspend fun update(cardRequest: DbCardRequest): DbCardResponse {
                TODO("Not yet implemented")
            }

            override suspend fun delete(cardRequest: DbCardGuidRequest): DbCardResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
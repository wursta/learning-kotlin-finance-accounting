package local.learning.common.repo

import local.learning.common.repo.expense.*

interface IExpenseRepository {
    suspend fun create(request: DbExpenseRequest): DbExpenseResponse
    suspend fun read(request: DbExpenseGuidRequest): DbExpenseResponse
    suspend fun update(request: DbExpenseRequest): DbExpenseResponse
    suspend fun delete(request: DbExpenseGuidRequest): DbExpenseResponse
    suspend fun search(request: DbExpenseSearchRequest): DbExpensesSearchResponse
    suspend fun stats(request: DbExpenseStatisticRequest): DbExpensesStatisticResponse

    companion object {
        val NONE = object: IExpenseRepository {
            override suspend fun create(request: DbExpenseRequest): DbExpenseResponse {
                TODO("Not yet implemented")
            }

            override suspend fun read(request: DbExpenseGuidRequest): DbExpenseResponse {
                TODO("Not yet implemented")
            }

            override suspend fun update(request: DbExpenseRequest): DbExpenseResponse {
                TODO("Not yet implemented")
            }

            override suspend fun delete(request: DbExpenseGuidRequest): DbExpenseResponse {
                TODO("Not yet implemented")
            }

            override suspend fun search(request: DbExpenseSearchRequest): DbExpensesSearchResponse {
                TODO("Not yet implemented")
            }

            override suspend fun stats(request: DbExpenseStatisticRequest): DbExpensesStatisticResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
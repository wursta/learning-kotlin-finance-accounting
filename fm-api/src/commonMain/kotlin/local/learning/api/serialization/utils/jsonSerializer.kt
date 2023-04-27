package local.learning.api.serialization.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import local.learning.api.models.*

internal val infos = listOf(
    // Cards requests
    info(CardCreateRequestDto::class, IRequestDto::class, "cardCreate") { copy(requestType = it) },
    info(CardReadRequestDto::class, IRequestDto::class, "cardRead") { copy(requestType = it) },
    info(CardUpdateRequestDto::class, IRequestDto::class, "cardUpdate") { copy(requestType = it) },
    info(CardDeleteRequestDto::class, IRequestDto::class, "cardDelete") { copy(requestType = it) },

    // Cards responses
    info(CardCreateResponseDto::class, IResponseDto::class, "cardCreate") { copy(responseType = it) },
    info(CardReadResponseDto::class, IResponseDto::class, "cardRead") { copy(responseType = it) },
    info(CardUpdateResponseDto::class, IResponseDto::class, "cardUpdate") { copy(responseType = it) },
    info(CardDeleteResponseDto::class, IResponseDto::class, "cardDelete") { copy(responseType = it) },

    // Expense requests
    info(ExpenseCreateRequestDto::class, IRequestDto::class, "expenseCreate") { copy(requestType = it) },
    info(ExpenseReadRequestDto::class, IRequestDto::class, "expenseRead") { copy(requestType = it) },
    info(ExpenseUpdateRequestDto::class, IRequestDto::class, "expenseUpdate") { copy(requestType = it) },
    info(ExpenseDeleteRequestDto::class, IRequestDto::class, "expenseDelete") { copy(requestType = it) },
    info(ExpenseSearchRequestDto::class, IRequestDto::class, "expensesSearch") { copy(requestType = it) },
    info(ExpenseStatsRequestDto::class, IRequestDto::class, "expensesStats") { copy(requestType = it) },

    // Expense responses
    info(ExpenseCreateResponseDto::class, IResponseDto::class, "expenseCreate") { copy(responseType = it) },
    info(ExpenseReadResponseDto::class, IResponseDto::class, "expenseRead") { copy(responseType = it) },
    info(ExpenseUpdateResponseDto::class, IResponseDto::class, "expenseUpdate") { copy(responseType = it) },
    info(ExpenseDeleteResponseDto::class, IResponseDto::class, "expenseDelete") { copy(responseType = it) },
    info(ExpenseSearchResponseDto::class, IResponseDto::class, "expensesSearch") { copy(responseType = it) },
    info(ExpenseStatsResponseDto::class, IResponseDto::class, "expensesStats") { copy(responseType = it) },
)

val jsonSerializer = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        setupPolymorphic()
    }
}
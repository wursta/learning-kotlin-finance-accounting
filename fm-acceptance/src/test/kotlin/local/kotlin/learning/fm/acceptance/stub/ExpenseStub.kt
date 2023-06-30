package local.kotlin.learning.fm.acceptance.stub

import local.learning.api.models.*

object ExpenseStub {
    fun getCreateRequest(stubCase: ExpenseRequestWorkModeDto.StubCase): ExpenseCreateRequestDto {
        return ExpenseCreateRequestDto(
            requestType = "expenseCreate",
            requestId = "uniqueId",
            workMode = ExpenseRequestWorkModeDto(
                mode = ExpenseRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getReadRequest(stubCase: ExpenseRequestWorkModeDto.StubCase): ExpenseReadRequestDto {
        return ExpenseReadRequestDto(
            requestType = "expenseRead",
            requestId = "uniqueId",
            workMode = ExpenseRequestWorkModeDto(
                mode = ExpenseRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getUpdateRequest(stubCase: ExpenseRequestWorkModeDto.StubCase): ExpenseUpdateRequestDto {
        return ExpenseUpdateRequestDto(
            requestType = "expenseUpdate",
            requestId = "uniqueId",
            workMode = ExpenseRequestWorkModeDto(
                mode = ExpenseRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getDeleteRequest(stubCase: ExpenseRequestWorkModeDto.StubCase): ExpenseDeleteRequestDto {
        return ExpenseDeleteRequestDto(
            requestType = "expenseUpdate",
            requestId = "uniqueId",
            workMode = ExpenseRequestWorkModeDto(
                mode = ExpenseRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getSearchRequest(stubCase: ExpenseRequestWorkModeDto.StubCase): ExpenseSearchRequestDto {
        return ExpenseSearchRequestDto(
            requestType = "expensesSearch",
            requestId = "uniqueId",
            workMode = ExpenseRequestWorkModeDto(
                mode = ExpenseRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    fun getStatisticRequest(stubCase: ExpenseRequestWorkModeDto.StubCase): ExpenseStatsRequestDto {
        return ExpenseStatsRequestDto(
            requestType = "expensesStats",
            requestId = "uniqueId",
            workMode = ExpenseRequestWorkModeDto(
                mode = ExpenseRequestWorkModeDto.Mode.STUB,
                stubCase = stubCase
            )
        )
    }

    val createResponse = ExpenseCreateResponseDto(
        responseType = "expenseCreate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-01-01T14:46:04",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val readResponse = ExpenseReadResponseDto(
        responseType = "expenseRead",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-01-01T14:46:04",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val updateResponse = ExpenseUpdateResponseDto(
        responseType = "expenseUpdate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-01-01T14:46:04",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val deleteResponse = ExpenseDeleteResponseDto(
        responseType = "expenseDelete",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-01-01T14:46:04",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val searchResponse = ExpenseSearchResponseDto(
        responseType = "expensesSearch",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        expenses = listOf(
            ExpenseObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                createDt = "2023-01-01T14:46:04",
                amount = 100.0,
                card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
            ),
            ExpenseObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                createDt = "2023-01-01T14:46:04",
                amount = 205.32,
                card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
            )
        )
    )

    val statsResponse = ExpenseStatsResponseDto(
        responseType = "expensesStats",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        errors = emptyList(),
        total = 105304.0,
        summary = listOf(
            ExpenseStatSummaryItemDto(
                amount = 20345.0,
                category = CategoryObjectDto(
                    guid = "5410bdaf-834a-4ca6-9044-ee25d5a7164c",
                    name ="Кредит"
                )
            ),
            ExpenseStatSummaryItemDto(
                amount = 30342.5,
                category = CategoryObjectDto(
                    guid = "5410bdaf-834a-4ca6-9044-ee25d8a7164c",
                    name ="Еда"
                )
            ),
            ExpenseStatSummaryItemDto(
                amount = 54616.5,
                category = CategoryObjectDto(
                    guid = "5410bdaf-834a-4ga6-9044-ee25d8a7164c",
                    name ="Одежда"
                )
            )
        )
    )
}
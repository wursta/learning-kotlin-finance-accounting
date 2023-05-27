package local.kotlin.learning.fm.acceptance.stub

import local.learning.api.models.*

object ExpenseStub {
    val createRequest = ExpenseCreateRequestDto(
        requestType = "expenseCreate",
        requestId = "uniqueId",
        expense = ExpenseCreateObjectDto(
            createDt = "2023-05-11T18:18:47.042Z",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
    )

    val readRequest = ExpenseReadRequestDto(
        requestType = "expenseRead",
        requestId = "uniqueId",
        guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )

    val updateRequest = ExpenseUpdateRequestDto(
        requestType = "expenseUpdate",
        requestId = "uniqueId",
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-05-11T18:18:47.042Z",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
    )

    val deleteRequest = ExpenseDeleteRequestDto(
        requestType = "expenseDelete",
        requestId = "uniqueId",
        guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )

    val searchRequest = ExpenseSearchRequestDto(
        requestType = "expensesSearch",
        requestId = "uniqueId",
        dateFrom = "2023-05-12T17:41:06.765Z",
        dateTo = "2023-05-12T17:41:06.765Z",
        cards = listOf("3fa85f64-5717-4562-b3fc-2c963f66afa6")
    )

    val statsRequest = ExpenseStatsRequestDto(
        requestType = "expensesStats",
        requestId = "uniqueId",
        dateFrom = "2023-05-12T17:42:33.806Z",
        dateTo = "2023-05-12T17:42:33.806Z"
    )

    val createResponse = ExpenseCreateResponseDto(
        responseType = "expenseCreate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-05-27T20:50:58.763700736",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val readResponse = ExpenseReadResponseDto(
        responseType = "expenseRead",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-05-27T20:50:58.763700736",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val updateResponse = ExpenseReadResponseDto(
        responseType = "expenseUpdate",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-05-27T20:50:58.763700736",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val deleteResponse = ExpenseReadResponseDto(
        responseType = "expenseDelete",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        expense = ExpenseObjectDto(
            guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            createDt = "2023-05-27T20:50:58.763700736",
            amount = 100.0,
            card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
        )
    )

    val searchResponse = ExpenseSearchResponseDto(
        responseType = "expensesSearch",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
        expenses = listOf(
            ExpenseObjectDto(
                guid = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                createDt = "2023-05-27T20:50:58.763700736",
                amount = 100.0,
                card = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                category = "5410bdaf-834a-4ca6-9044-ee25d5a7164c"
            )
        )
    )

    val statsResponse = ExpenseStatsResponseDto(
        responseType = "expensesStats",
        requestId = "uniqueId",
        result = ResponseResultDto.SUCCESS,
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
                    guid = "5410bdaf-834a-4ca6-9044-ee25d8a7164c",
                    name ="Одежда"
                )
            )
        )
    )
}
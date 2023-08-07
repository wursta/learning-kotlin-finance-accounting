import com.arcadedb.query.sql.executor.EmptyResult
import com.arcadedb.remote.RemoteDatabase
import com.benasher44.uuid.uuid4
import local.learning.common.errors.ErrorCode
import local.learning.common.errors.ErrorGroup
import local.learning.common.models.Error
import local.learning.common.models.LockGuid
import local.learning.common.models.category.Category
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseGuid
import local.learning.common.models.expense.ExpenseStatistic
import local.learning.common.models.expense.ExpenseSummaryByCategory
import local.learning.common.repo.IExpenseRepository
import local.learning.common.repo.expense.*
import java.math.BigDecimal

class ExpenseArcadeDbRepository(
    private val host: String,
    private val port: Int,
    private val dbName: String,
    private val userName: String,
    private val userPassword: String,
    val randomUuid: () -> String = { uuid4().toString() }
) : IExpenseRepository {
    private val db by lazy {
        RemoteDatabase(host, port, dbName, userName, userPassword)
    }

    fun initRepo(vararg expenses: Expense) {
        expenses.forEach { expense ->
            db.command(
                "sql",
                "CREATE VERTEX expense SET " +
                        "guid = :guid, " +
                        "createDT = :createDT, " +
                        "amount = :amount, " +
                        "cardGuid = :cardGuid, " +
                        "categoryGuid = :categoryGuid, " +
                        "lockGuid = :lockGuid",
                hashMapOf(
                    Pair("guid", expense.guid.asString()),
                    Pair("createDT", expense.createDT.toEpochMilliseconds()),
                    Pair("amount", expense.amount.toDouble()),
                    Pair("cardGuid", expense.cardGuid.asString()),
                    Pair("categoryGuid", expense.categoryGuid.asString()),
                    Pair("lockGuid", expense.lockGuid.asString())
                )
            )
        }
    }

    override suspend fun create(request: DbExpenseRequest): DbExpenseResponse {
        val key = randomUuid()
        val expense = request.expense.copy(guid = ExpenseGuid(key), lockGuid = LockGuid(randomUuid()))

        try {
            val resultSet = db.command(
                "sql",
                "CREATE VERTEX expense SET " +
                        "guid = :guid, " +
                        "createDT = :createDT, " +
                        "amount = :amount, " +
                        "cardGuid = :cardGuid, " +
                        "categoryGuid = :categoryGuid, " +
                        "lockGuid = :lockGuid",
                hashMapOf(
                    Pair("guid", expense.guid.asString()),
                    Pair("createDT", expense.createDT.toEpochMilliseconds()),
                    Pair("amount", expense.amount.toDouble()),
                    Pair("cardGuid", expense.cardGuid.asString()),
                    Pair("categoryGuid", expense.categoryGuid.asString()),
                    Pair("lockGuid", expense.lockGuid.asString())
                )
            )
            val record = resultSet.nextIfAvailable()
            return DbExpenseResponse(record.toExpenseInternal(), true)
        } catch (e: Throwable) {
            return DbExpenseResponse(
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

    override suspend fun read(request: DbExpenseGuidRequest): DbExpenseResponse {
        val key = request.guid.takeIf { it != ExpenseGuid.NONE }?.asString() ?: return resultErrorEmptyGuid

        try {
            val resultSet = db.command(
                "sql",
                "SELECT FROM expense WHERE guid = :guid",
                hashMapOf(
                    Pair("guid", key)
                )
            )

            val record = resultSet.nextIfAvailable()

            if (record is EmptyResult) {
                return resultErrorNotFound
            }

            return DbExpenseResponse(record.toExpenseInternal(), true)
        } catch (e: Throwable) {
            return DbExpenseResponse(
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

    override suspend fun update(request: DbExpenseRequest): DbExpenseResponse {
        val key = request.expense.guid.takeIf { it != ExpenseGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest =
            request.expense.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        try {
            val resultSetForUpdate = db.command(
                "sql",
                "SELECT FROM expense WHERE guid = :guid",
                hashMapOf(
                    Pair("guid", key)
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
                "UPDATE expense SET " +
                        "createDT = :createDT, " +
                        "amount = :amount, " +
                        "cardGuid = :cardGuid, " +
                        "categoryGuid = :categoryGuid, " +
                        "lockGuid = :newLockGuid " +
                        "RETURN AFTER " +
                        "WHERE guid = :guid AND lockGuid = :oldLockGuid",
                hashMapOf(
                    Pair("guid", key),
                    Pair("createDT", request.expense.createDT.toEpochMilliseconds()),
                    Pair("amount", request.expense.amount.toDouble()),
                    Pair("cardGuid", request.expense.cardGuid.asString()),
                    Pair("categoryGuid", request.expense.categoryGuid.asString()),
                    Pair("newLockGuid", randomUuid()),
                    Pair("oldLockGuid", lockFromRequest)
                )
            )

            val record = resultSet.nextIfAvailable()

            if (record is EmptyResult) {
                return resultErrorInvalidLock
            }

            return DbExpenseResponse(record.toExpenseInternal(), true)
        } catch (e: Throwable) {
            return DbExpenseResponse(
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

    override suspend fun delete(request: DbExpenseGuidRequest): DbExpenseResponse {
        val key = request.guid.takeIf { it != ExpenseGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest = request.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        try {
            val resultSetForDelete = db.command(
                "sql",
                "SELECT FROM expense WHERE guid = :guid",
                hashMapOf(
                    Pair("guid", key)
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
                "DELETE FROM expense " +
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

            return DbExpenseResponse(record.toExpenseInternal(), true)
        } catch (e: Throwable) {
            return DbExpenseResponse(
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

    override suspend fun search(request: DbExpenseSearchRequest): DbExpensesSearchResponse {
        val clauseParts = mutableListOf<String>()
        val params = hashMapOf<String, Any>()
        if (request.amountFrom != null) {
            clauseParts.add("amount >= :amountFrom")
            params["amountFrom"] = request.amountFrom!!.toDouble()
        }
        if (request.amountTo != null) {
            clauseParts.add("amount <= :amountTo")
            params["amountTo"] = request.amountTo!!.toDouble()
        }
        if (request.dateFrom != null) {
            clauseParts.add("createDT >= :dateFrom")
            params["dateFrom"] = request.dateFrom!!.toEpochMilliseconds()
        }
        if (request.dateTo != null) {
            clauseParts.add("createDT <= :dateTo")
            params["dateTo"] = request.dateTo!!.toEpochMilliseconds()
        }

        var sql = "SELECT FROM expense"
        if (clauseParts.isNotEmpty()) {
            sql += " WHERE ${clauseParts.joinToString(" AND ")}"
        }

        try {
            val resultSet = db.command("sql", sql, params)

            val filteredExpenses = mutableListOf<Expense>()
            resultSet.forEach { record ->
                filteredExpenses.add(record.toExpenseInternal())
            }

            return DbExpensesSearchResponse(filteredExpenses.toList(), true)
        } catch (e: Throwable) {
            return DbExpensesSearchResponse(
                success = false,
                errors = listOf(
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

    override suspend fun stats(request: DbExpenseStatisticRequest): DbExpensesStatisticResponse {
        try {
            val clauseParts = mutableListOf<String>()
            val params = hashMapOf<String, Any>()

            if (request.dateFrom != null) {
                clauseParts.add("createDT >= :dateFrom")
                params["dateFrom"] = request.dateFrom!!.toEpochMilliseconds()
            }
            if (request.dateTo != null) {
                clauseParts.add("createDT <= :dateTo")
                params["dateTo"] = request.dateTo!!.toEpochMilliseconds()
            }

            var clauseSql = ""
            if (clauseParts.isNotEmpty()) {
                clauseSql += " WHERE ${clauseParts.joinToString(" AND ")}"
            }

            val totalResultSet = db.command(
                "sql",
                "SELECT sum(amount) as total FROM expense$clauseSql",
                params
            )

            val totalRecord = totalResultSet.nextIfAvailable()
            var total = BigDecimal(0)
            if (totalRecord !is EmptyResult) {
                total = BigDecimal(totalRecord.getProperty<Double>("total"))
            }

            val summaryByCategory = mutableListOf<ExpenseSummaryByCategory>()

            val groupedTotalResultSet = db.command(
                "sql",
                "SELECT sum(amount) as categoryTotal, categoryGuid FROM expense$clauseSql GROUP BY categoryGuid",
                params
            )
            groupedTotalResultSet.forEach {record ->
                summaryByCategory.add(
                    ExpenseSummaryByCategory(
                        category = Category(guid = CategoryGuid(record.getProperty("categoryGuid"))),
                        amount = BigDecimal(record.getProperty<Double>("categoryTotal"))
                    )
                )
            }

            return DbExpensesStatisticResponse(ExpenseStatistic(total = total, summaryByCategory = summaryByCategory), true)
        } catch (e: Throwable) {
            return DbExpensesStatisticResponse(
                expenseStatistic = ExpenseStatistic(),
                success = false,
                errors = listOf(
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

    companion object {
        val resultErrorEmptyGuid = DbExpenseResponse(
            expense = null,
            success = false,
            errors = listOf(
                Error(
                    code = ErrorCode.EMPTY_GUID,
                    group = ErrorGroup.VALIDATION,
                    field = "guid",
                    message = "Expense guid must not be null or blank"
                )
            )
        )

        val resultErrorEmptyLock = DbExpenseResponse(
            expense = null,
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

        val resultErrorInvalidLock = DbExpenseResponse(
            expense = null,
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

        val resultErrorNotFound = DbExpenseResponse(
            expense = null,
            success = false,
            errors = listOf(
                Error(
                    code = ErrorCode.NOT_FOUND,
                    group = ErrorGroup.VALIDATION,
                    field = "guid",
                    message = "Expense not found"
                )
            )
        )
    }
}
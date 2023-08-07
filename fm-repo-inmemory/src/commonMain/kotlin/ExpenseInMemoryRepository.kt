package local.learning.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
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
import local.learning.repo.inmemory.models.ExpenseEntity
import java.math.BigDecimal
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ExpenseInMemoryRepository(
    initObjects: List<Expense> = emptyList(),
    ttl: Duration = 10.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : IExpenseRepository {

    private val mutex = Mutex()
    private val cache = Cache.Builder<String, ExpenseEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            cache.put(it.guid.asString(), ExpenseEntity(it))
        }
    }

    override suspend fun create(request: DbExpenseRequest): DbExpenseResponse {
        val key = randomUuid()
        val expense = request.expense.copy(guid = ExpenseGuid(key), lockGuid = LockGuid(randomUuid()))
        val expenseEntity = ExpenseEntity(expense)
        cache.put(key, expenseEntity)
        return DbExpenseResponse(expense, true)
    }

    override suspend fun read(request: DbExpenseGuidRequest): DbExpenseResponse {
        val key = request.guid.takeIf { it != ExpenseGuid.NONE }?.asString() ?: return resultErrorEmptyGuid

        val expenseEntity = cache.get(key) ?: return resultErrorNotFound

        return DbExpenseResponse(expenseEntity.toInternal(), true)
    }

    override suspend fun update(request: DbExpenseRequest): DbExpenseResponse {
        val key = request.expense.guid.takeIf { it != ExpenseGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest = request.expense.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldEntity = cache.get(key) ?: return resultErrorNotFound
            if (oldEntity.lockGuid != lockFromRequest) {
                return resultErrorInvalidLock
            }
            val newExpenseEntity = ExpenseEntity(request.expense)
            newExpenseEntity.lockGuid = randomUuid()
            cache.put(key, newExpenseEntity)
            return DbExpenseResponse(newExpenseEntity.toInternal(), true)
        }
    }

    override suspend fun delete(request: DbExpenseGuidRequest): DbExpenseResponse {
        val key = request.guid.takeIf { it != ExpenseGuid.NONE }?.asString() ?: return resultErrorEmptyGuid
        val lockFromRequest = request.lockGuid.takeIf { it != LockGuid.NONE }?.asString() ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldEntity = cache.get(key) ?: return resultErrorNotFound
            if (oldEntity.lockGuid != lockFromRequest) {
                return resultErrorInvalidLock
            }
            cache.invalidate(key)
            return DbExpenseResponse(oldEntity.toInternal(), true)
        }
    }

    override suspend fun search(request: DbExpenseSearchRequest): DbExpensesSearchResponse {
        val filteredExpenses = cache.asMap().filter {
            val expense = it.value.toInternal()

            if (request.amountFrom != null && !searchAmountFromPredicate(expense, request.amountFrom!!)) {
                return@filter false
            }

            if (request.amountTo != null && !searchAmountToPredicate(expense, request.amountTo!!)) {
                return@filter false
            }

            if (request.dateFrom != null && !searchDateFromPredicate(expense, request.dateFrom!!)) {
                return@filter false
            }

            if (request.dateTo != null && !searchDateToPredicate(expense, request.dateTo!!)) {
                return@filter false
            }

            return@filter true
        }.map {
            it.value.toInternal()
        }

        return DbExpensesSearchResponse(filteredExpenses, true)
    }

    override suspend fun stats(request: DbExpenseStatisticRequest): DbExpensesStatisticResponse {
        var total: BigDecimal = BigDecimal.ZERO

        return mutex.withLock {
            // Create summary-category map for easy computing
            val summaryCategoryMap = cache.asMap().asSequence().associateBy(
                { it.value.categoryGuid },
                {
                    ExpenseSummaryByCategory(
                        category = Category(
                            guid = it.value.categoryGuid?.let { guid -> CategoryGuid(guid) } ?: CategoryGuid.NONE
                        ),
                        amount = BigDecimal.ZERO
                    )
                }
            )

            // Summarize amounts to expense category in map
            cache.asMap().forEach { guid, expense ->
                val expenseAmount = expense.amount ?: BigDecimal.ZERO
                val summaryByCategory = summaryCategoryMap[expense.categoryGuid]
                    ?: throw Exception("This can't be! Category not found in map, but just now we collect them...")

                if (request.dateFrom != null && !searchDateFromPredicate(expense.toInternal(), request.dateFrom!!)) {
                    return@forEach
                }
                if (request.dateTo != null && !searchDateToPredicate(expense.toInternal(), request.dateTo!!)) {
                    return@forEach
                }

                total = total.plus(expenseAmount)
                summaryByCategory.amount = summaryByCategory.amount.plus(expenseAmount)
            }

            return DbExpensesStatisticResponse(
                ExpenseStatistic(
                    total = total,
                    summaryByCategory = summaryCategoryMap.filter { it.value.amount > BigDecimal.ZERO }.map { it.value }
                ), true
            )
        }
    }

    companion object {
        val searchAmountFromPredicate: (Expense, BigDecimal) -> Boolean = p@{ expense, amountFrom ->
            return@p expense.amount.compareTo(amountFrom) >= 0
        }

        val searchAmountToPredicate: (Expense, BigDecimal) -> Boolean = p@{ expense, amountTo ->
            return@p expense.amount.compareTo(amountTo) <= 0
        }

        val searchDateFromPredicate: (Expense, Instant) -> Boolean = p@{ expense, dateFrom ->
            return@p expense.createDT.compareTo(dateFrom) >= 0
        }

        val searchDateToPredicate: (Expense, Instant) -> Boolean = p@{ expense, dateTo ->
            return@p expense.createDT.compareTo(dateTo) <= 0
        }

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
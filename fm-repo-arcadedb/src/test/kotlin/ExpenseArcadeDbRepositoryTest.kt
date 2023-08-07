package local.learning.repo.arcadedb

import ArcadeDbContainer
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import local.learning.common.models.LockGuid
import local.learning.common.models.card.CardGuid
import local.learning.common.models.category.CategoryGuid
import local.learning.common.models.expense.Expense
import local.learning.common.models.expense.ExpenseGuid
import local.learning.common.repo.expense.DbExpenseGuidRequest
import local.learning.common.repo.expense.DbExpenseRequest
import local.learning.common.repo.expense.DbExpenseSearchRequest
import local.learning.common.repo.expense.DbExpenseStatisticRequest
import org.junit.Before
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseArcadeDbRepositoryTest {
    private val repo: ExpenseArcadeDbRepository
    private fun randomUuid(): String = uuid4().toString()

    init {
        val host = ArcadeDbContainer.container.host
        val port = ArcadeDbContainer.container.getMappedPort(2480)
        val dbName = "FMTest"
        val userName = ArcadeDbContainer.username
        val userPassword = ArcadeDbContainer.password

        repo = ExpenseArcadeDbRepository(
            settings = ArcadeDbSettings(
                host = host,
                port = port,
                dbName = dbName,
                userName = userName,
                userPassword = userPassword
            )
        )

        ArcadeDbSchema.initialize(host, port, dbName, userName, userPassword)
    }

    @Before
    fun beforeEach() {
        val host = ArcadeDbContainer.container.host
        val port = ArcadeDbContainer.container.getMappedPort(2480)
        val dbName = "FMTest"
        val userName = ArcadeDbContainer.username
        val userPassword = ArcadeDbContainer.password

        ArcadeDbSchema.clear(host, port, dbName, userName, userPassword)
        repo.initRepo(
            Expense(
                guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                createDT = Instant.parse("2023-01-01T14:46:04Z"),
                amount = BigDecimal(1234.56),
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"),
                lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
            ),
            Expense(
                guid = ExpenseGuid("bbd74256-e582-4e1c-aa19-a05cac64c589"),
                createDT = Instant.parse("2023-01-02T14:46:04Z"),
                amount = BigDecimal(550.56),
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"),
                lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
            ),
            Expense(
                guid = ExpenseGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                createDT = Instant.parse("2023-01-03T14:46:04Z"),
                amount = BigDecimal(100),
                cardGuid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                categoryGuid = CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a71645"),
                lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
            )
        )
    }

    @Test
    fun create() = runTest {
        val cardGuid = CardGuid(randomUuid())
        val categoryGuid = CategoryGuid(randomUuid())

        val dbRequest = DbExpenseRequest(
            Expense(
                createDT = Instant.parse("2023-05-10T11:43:03Z"),
                amount = BigDecimal(1113.44),
                cardGuid = cardGuid,
                categoryGuid = categoryGuid
            )
        )
        val result = repo.create(dbRequest)

        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertNotEquals(ExpenseGuid.NONE, result.expense?.guid)
        assertEquals(Instant.parse("2023-05-10T11:43:03Z"), result.expense?.createDT)
        assertEquals(BigDecimal(1113.44), result.expense?.amount)
        assertEquals(cardGuid, result.expense?.cardGuid)
        assertEquals(categoryGuid, result.expense?.categoryGuid)
    }

    @Test
    fun read() = runTest {
        val dbRequest = DbExpenseGuidRequest(ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"))
        val result = repo.read(dbRequest)

        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertEquals(ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"), result.expense?.guid)
        assertEquals(Instant.parse("2023-01-01T14:46:04Z"), result.expense?.createDT)
        assertEquals(BigDecimal(1234.56), result.expense?.amount)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), result.expense?.cardGuid)
        assertEquals(CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"), result.expense?.categoryGuid)
    }

    @Test
    fun update() = runTest {
        val cardGuid = CardGuid(randomUuid())
        val categoryGuid = CategoryGuid(randomUuid())

        val dbRequest = DbExpenseRequest(
            Expense(
                guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                createDT = Instant.parse("2023-05-10T11:43:03Z"),
                amount = BigDecimal(1113.44),
                cardGuid = cardGuid,
                categoryGuid = categoryGuid,
                lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
            )
        )

        val result = repo.update(dbRequest)
        println(result)
        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertEquals(ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"), result.expense?.guid)
        assertEquals(Instant.parse("2023-05-10T11:43:03Z"), result.expense?.createDT)
        assertEquals(BigDecimal(1113.44), result.expense?.amount)
        assertEquals(cardGuid, result.expense?.cardGuid)
        assertEquals(categoryGuid, result.expense?.categoryGuid)
    }

    @Test
    fun delete() = runTest {
        val dbRequest = DbExpenseGuidRequest(
            guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"),
            lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
        )
        val deleteResult = repo.delete(dbRequest)

        assertEquals(true, deleteResult.success)
        assertEquals(0, deleteResult.errors.size)
        assertEquals(ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"), deleteResult.expense?.guid)
        assertEquals(Instant.parse("2023-01-01T14:46:04Z"), deleteResult.expense?.createDT)
        assertEquals(BigDecimal(1234.56), deleteResult.expense?.amount)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), deleteResult.expense?.cardGuid)
        assertEquals(CategoryGuid("5410bdaf-834a-4ca6-9044-ee25d5a7164c"), deleteResult.expense?.categoryGuid)

        val readResult = repo.read(dbRequest)

        assertEquals(false, readResult.success)
        assertNotEquals(0, readResult.errors.size)
    }

    @Test
    fun tryUpdateWithInvalidLock() = runTest {
        val dbRequest = DbExpenseRequest(
            Expense(
                guid = ExpenseGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                createDT = Instant.parse("2023-05-10T11:43:03Z"),
                amount = BigDecimal(1113.44),
                cardGuid = CardGuid(randomUuid()),
                categoryGuid = CategoryGuid(randomUuid()),
                lockGuid = LockGuid("invalid lock")
            )
        )

        val result = repo.update(dbRequest)

        assertEquals(false, result.success)
        assertEquals(true, result.errors.isNotEmpty())
    }

    @Test
    fun search() = runTest {
        val searchAmountCaseRequest = DbExpenseSearchRequest(
            amountFrom = BigDecimal.ZERO,
            amountTo = BigDecimal(200),
            dateFrom = null,
            dateTo = null,
            createdBy = null
        )

        val resultAmountCase = repo.search(searchAmountCaseRequest)
        println(resultAmountCase)
        assertEquals(true, resultAmountCase.success)
        assertEquals(0, resultAmountCase.errors.size)
        assertEquals(1, resultAmountCase.expenses.size)

        val searchDatesCaseRequest = DbExpenseSearchRequest(
            amountFrom = null,
            amountTo = null,
            dateFrom = Instant.parse("2023-01-01T00:00:00Z"),
            dateTo = Instant.parse("2023-01-02T23:59:59Z"),
            createdBy = null
        )

        val resultDatesCase = repo.search(searchDatesCaseRequest)

        assertEquals(true, resultDatesCase.success)
        assertEquals(0, resultDatesCase.errors.size)
        assertEquals(2, resultDatesCase.expenses.size)
    }

    @Test
    fun stats() = runTest {
        val request = DbExpenseStatisticRequest(
            dateFrom = Instant.parse("2023-01-01T00:00:00Z"),
            dateTo = Instant.parse("2023-01-03T23:59:59Z"),
            createdBy = null
        )

        val result = repo.stats(request)

        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertEquals(2, result.expenseStatistic.summaryByCategory.size)
    }
}
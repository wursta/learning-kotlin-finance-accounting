import com.benasher44.uuid.uuid4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import local.learning.common.models.LockGuid
import local.learning.common.models.bank.BankGuid
import local.learning.common.models.card.Card
import local.learning.common.models.card.CardGuid
import local.learning.common.repo.card.DbCardGuidRequest
import local.learning.common.repo.card.DbCardRequest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CardArcadeDbRepositoryTest {
    private val repo: CardArcadeDbRepository
    private fun randomUuid(): String = uuid4().toString()

    init {
        val host = ArcadeDbContainer.container.host
        val port = ArcadeDbContainer.container.getMappedPort(2480)
        val dbName = "FMTest"
        val userName = ArcadeDbContainer.username
        val userPassword = ArcadeDbContainer.password

        repo = CardArcadeDbRepository(
            host = host,
            port = port,
            dbName = dbName,
            userName = userName,
            userPassword = userPassword
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
            Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "5191891428863955",
                validFor = "2023-04",
                owner = "SAZONOV MIKHAIL",
                bankGuid = BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
            )
        )
    }

    @Test
    fun create() = runTest {
        val bankGuid = BankGuid(randomUuid())
        val dbRequest = DbCardRequest(
            Card(
                number = "test number",
                validFor = "test valid for",
                owner = "test owner",
                bankGuid = bankGuid
            )
        )
        val result = repo.create(dbRequest)

        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertNotEquals(CardGuid.NONE, result.card?.guid)
        assertEquals("test number", result.card?.number)
        assertEquals("test valid for", result.card?.validFor)
        assertEquals("test owner", result.card?.owner)
        assertEquals(bankGuid, result.card?.bankGuid)
    }

    @Test
    fun read() = runTest {
        val dbRequest = DbCardGuidRequest(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"))
        val result = repo.read(dbRequest)

        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), result.card?.guid)
        assertEquals("5191891428863955", result.card?.number)
        assertEquals("2023-04", result.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", result.card?.owner)
        assertEquals(BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"), result.card?.bankGuid)

    }

    @Test
    fun update() = runTest {
        val dbRequest = DbCardRequest(
            Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "test number1",
                validFor = "test valid for1",
                owner = "test owner1",
                bankGuid = BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
            )
        )

        val result = repo.update(dbRequest)

        assertEquals(true, result.success)
        assertEquals(0, result.errors.size)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), result.card?.guid)
        assertEquals("test number1", result.card?.number)
        assertEquals("test valid for1", result.card?.validFor)
        assertEquals("test owner1", result.card?.owner)
        assertEquals(BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"), result.card?.bankGuid)
    }

    @Test
    fun delete() = runTest {
        val dbRequest = DbCardGuidRequest(
            guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
            lockGuid = LockGuid("9e6aaa2f-1c8d-4279-b525-4ef391589ede")
        )
        val deleteResult = repo.delete(dbRequest)

        assertEquals(true, deleteResult.success)
        assertEquals(0, deleteResult.errors.size)
        assertEquals(CardGuid("1598044e-5259-11e9-8647-d663bd873d93"), deleteResult.card?.guid)
        assertEquals("5191891428863955", deleteResult.card?.number)
        assertEquals("2023-04", deleteResult.card?.validFor)
        assertEquals("SAZONOV MIKHAIL", deleteResult.card?.owner)
        assertEquals(BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"), deleteResult.card?.bankGuid)

        val readResult = repo.read(dbRequest)

        assertEquals(false, readResult.success)
        assertNotEquals(0, readResult.errors.size)
    }

    @Test
    fun tryUpdateWithInvalidLock() = runTest {
        val dbRequest = DbCardRequest(
            Card(
                guid = CardGuid("1598044e-5259-11e9-8647-d663bd873d93"),
                number = "test number1",
                validFor = "test valid for1",
                owner = "test owner1",
                bankGuid = BankGuid("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                lockGuid = LockGuid("invalid lock")
            )
        )

        val result = repo.update(dbRequest)

        assertEquals(false, result.success)
        assertEquals(true, result.errors.isNotEmpty())
    }
}